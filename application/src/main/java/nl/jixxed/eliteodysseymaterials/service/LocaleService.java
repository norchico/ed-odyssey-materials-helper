package nl.jixxed.eliteodysseymaterials.service;

import javafx.beans.binding.ListBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.jixxed.eliteodysseymaterials.constants.PreferenceConstants;
import nl.jixxed.eliteodysseymaterials.domain.*;
import nl.jixxed.eliteodysseymaterials.enums.ApplicationLocale;
import nl.jixxed.eliteodysseymaterials.enums.Asset;
import nl.jixxed.eliteodysseymaterials.enums.Data;
import nl.jixxed.eliteodysseymaterials.enums.OdysseyMaterial;
import nl.jixxed.eliteodysseymaterials.helper.CSVResourceBundle;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LocaleService {
    private static final String[] RESOURCE_BUNDLE_NAMES = {
            "blueprint/horizons/category",
            "blueprint/horizons/description",
            "blueprint/horizons/modifier",
            "blueprint/horizons/names",
            "blueprint/horizons/type.description",
            "blueprint/horizons/type.names",
            "blueprint/odyssey/category",
            "blueprint/odyssey/description",
            "blueprint/odyssey/modifier",
            "blueprint/odyssey/names",
            "blueprint/odyssey/tips",
            "engineer/names",
            "engineer/specialisation",
            "loadout/equipment",
            "loadout/modification",
            "loadout/stat.group",
            "loadout/stat.name",
            "loadout/stat.value",
            "material/horizons/category",
            "material/horizons/commodity",
            "material/horizons/commodity.type",
            "material/horizons/encoded",
            "material/horizons/manufactured",
            "material/horizons/raw",
            "material/horizons/spawn",
            "material/odyssey/asset",
            "material/odyssey/consumable",
            "material/odyssey/data",
            "material/odyssey/good",
            "material/odyssey/spawn",
            "material/trade",
            "application",
            "blueprint",
            "menu",
            "tab.bartender",
            "tab.engineer",
            "tab.loadout",
            "tab.overview",
            "tab.settings",
            "tab.trade",
            "tab.wishlist",
            "tooltip"
    };
    private static Locale currentLocale = Locale.ENGLISH;
    private static final ApplicationState APPLICATION_STATE = ApplicationState.getInstance();
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    private LocaleService() {
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setCurrentLocale(final Locale locale) {
        currentLocale = locale;
        ObservableResourceFactory.setResources(CSVResourceBundle.getResourceBundle( currentLocale,RESOURCE_BUNDLE_NAMES));
    }

    public static String getLocalizedStringForCurrentLocale(final String key, final Object... parameters) {
        return getLocalizedStringForLocale(getCurrentLocale(), key, parameters);
    }

    public static String getLocalizedStringForLocale(final Locale locale, final String key, final Object... parameters) {
        return getLocalizedString(locale, key, parameters);
    }


    private static String getLocalizedString(final Locale locale, final String key, final Object... parameters) {
        final Object[] localizedParams = Arrays.stream(parameters).map(LocaleService::localizeParameter).toArray(Object[]::new);
        return MessageFormat.format(CSVResourceBundle.getResourceBundle(locale,RESOURCE_BUNDLE_NAMES).getString(key), localizedParams);
    }

    public static StringBinding getStringBinding(final String key, final Object... parameters) {
        return ObservableResourceFactory.getStringBinding(() -> {
            final Object[] localizedParams = Arrays.stream(parameters).map(LocaleService::localizeParameter).toArray(Object[]::new);
            return MessageFormat.format(ObservableResourceFactory.getResources().getString(key), localizedParams);
        });
    }

    public static ObservableValue<String> getSupplierStringBinding(final String key, final Supplier<Object>... parameterSuppliers) {
        return ObservableResourceFactory.getStringBinding(() -> {
            final Object[] parameters = Arrays.stream(parameterSuppliers).map(Supplier::get).toArray(Object[]::new);
            return MessageFormat.format(ObservableResourceFactory.getResources().getString(key), parameters);
        });
    }

    public static StringBinding getStringBinding(final OdysseyMaterial odysseyMaterial) {
        return ObservableResourceFactory.getStringBinding(() -> MessageFormat.format(ObservableResourceFactory.getResources().getString(odysseyMaterial.getLocalizationKey()), new Object[0]) + (odysseyMaterial.isIllegal() ? "   \u20E0 " : "") + (FavouriteService.isFavourite(odysseyMaterial) ? " \u2605" : ""));
    }

    public static StringBinding getStringBinding(final Supplier<String> supplier) {
        return ObservableResourceFactory.getStringBinding(supplier);
    }

    private static void addStatisticsToTooltip(final OdysseyMaterial odysseyMaterial, final StringBuilder builder) {
        final MaterialStatistic statistic = MaterialTrackingService.getMaterialStatistic(odysseyMaterial);
        builder.append("\n\n")
                .append("Economies:")
                .append("\n")
                .append(statistic.getEconomies().stream().map(economyStatistic -> economyStatistic.getEconomy() + "(" + economyStatistic.getAmount() + ")").collect(Collectors.joining(",")))
                .append("\n\n")
                .append("Most collected in settlements:")
                .append("\n")
                .append(statistic.getMostcollected().stream().map(settlementStatistic -> settlementStatistic.getAmount() + " - " + settlementStatistic.getSettlement() + " | " + settlementStatistic.getBody() + " | " + settlementStatistic.getSystem() + "(" + LocationService.calculateDistance(LocationService.getCurrentStarSystem(), new StarSystem(settlementStatistic.getSystem(), settlementStatistic.getX(), settlementStatistic.getY(), settlementStatistic.getZ())) + ")").collect(Collectors.joining("\n")))
                .append("\n\n")
                .append("Best runs:")
                .append("\n")
                .append(statistic.getBestrun().stream().map(settlementStatistic -> settlementStatistic.getAmount() + " - " + settlementStatistic.getSettlement() + " | " + settlementStatistic.getBody() + " | " + settlementStatistic.getSystem() + "(" + LocationService.calculateDistance(LocationService.getCurrentStarSystem(), new StarSystem(settlementStatistic.getSystem(), settlementStatistic.getX(), settlementStatistic.getY(), settlementStatistic.getZ())) + ")").collect(Collectors.joining("\n")));
    }

    public static StringBinding getToolTipStringBinding(final ModuleBlueprint recipe, final String localizationKey) {
        return ObservableResourceFactory.getStringBinding(() -> MessageFormat.format(ObservableResourceFactory.getResources().getString(localizationKey).translateEscapes(), recipe.getEngineers().stream().map(engineer -> ObservableResourceFactory.getResources().getString(engineer.getLocalizationKey())).collect(Collectors.joining(", "))));
    }

    public static StringBinding getToolTipStringBinding(final HorizonsEngineeringBlueprint recipe, final String localizationKey) {
        return ObservableResourceFactory.getStringBinding(
                () -> MessageFormat.format(ObservableResourceFactory.getResources().getString(localizationKey).translateEscapes(), recipe.getEngineers().stream().map(engineer -> ObservableResourceFactory.getResources().getString(engineer.getLocalizationKey())).collect(Collectors.joining(", ")))
        );
    }

    @SafeVarargs
    public static <T> ListBinding<T> getListBinding(final T... items) {
        return ObservableResourceFactory.getListBinding(items);
    }

    public static <T> ListBinding<T> getListBinding(final Supplier<T[]> supplier) {
        return ObservableResourceFactory.getListBinding(supplier);
    }

    private static Object localizeParameter(final Object parameter) {
        if (parameter instanceof LocalizationKey localizationKey) {
            return ObservableResourceFactory.getResources().getString(localizationKey.getKey());
        } else if (parameter instanceof OdysseyMaterial odysseyMaterial) {
            return ObservableResourceFactory.getResources().getString(odysseyMaterial.getLocalizationKey());
        } else if (parameter instanceof Number) {
            return parameter;
        }
        return parameter.toString();
    }

    static String getDataCharacterForCurrentARLocale() {
        final Locale locale = ApplicationLocale.valueOf(PreferencesService.getPreference(PreferenceConstants.AR_LOCALE, "ENGLISH")).getLocale();
        return Arrays.stream(Data.values())
                .filter(Predicate.not(Data::isUnknown))
                .map(data -> LocaleService.getLocalizedStringForLocale(locale, data.getLocalizationKey()))
                .flatMap(dataLocName -> Arrays.stream(dataLocName.split("")))
                .map(string -> (Locale.forLanguageTag("ru").equals(locale)) ? string : string.toUpperCase())
                .distinct()
                .sorted()
                .collect(Collectors.joining());
    }

    static String getAssetCharacterForCurrentARLocale() {
        final Locale locale = ApplicationLocale.valueOf(PreferencesService.getPreference(PreferenceConstants.AR_LOCALE, "ENGLISH")).getLocale();
        return Arrays.stream(Asset.values())
                .filter(Predicate.not(Asset::isUnknown))
                .map(asset -> LocaleService.getLocalizedStringForLocale(locale, asset.getLocalizationKey()))
                .flatMap(dataLocName -> Arrays.stream(dataLocName.split("")))
                .map(string -> (Locale.forLanguageTag("ru").equals(locale)) ? string : string.toUpperCase())
                .distinct()
                .sorted()
                .collect(Collectors.joining());
    }

    static String getTerminalCharacterForCurrentARLocale() {
        final Locale locale = ApplicationLocale.valueOf(PreferencesService.getPreference(PreferenceConstants.AR_LOCALE, "ENGLISH")).getLocale();
        return Arrays.stream(Data.values())
                .filter(Predicate.not(Data::isUnknown))
                .map(data -> LocaleService.getLocalizedStringForLocale(locale, data.getLocalizationKey()))
                .flatMap(dataLocName -> Arrays.stream(dataLocName.split("")))
                .map(string -> (Locale.forLanguageTag("ru").equals(locale)) ? string : string.toUpperCase())
                .distinct()
                .sorted()
                .collect(Collectors.joining());
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class LocalizationKey {
        private final String key;

        public static LocalizationKey of(final String key) {
            return new LocalizationKey(key);
        }
    }

}
