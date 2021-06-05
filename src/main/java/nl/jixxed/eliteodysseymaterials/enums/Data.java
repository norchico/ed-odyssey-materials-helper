package nl.jixxed.eliteodysseymaterials.enums;

public enum Data {
    ACCIDENTLOGS("Accident Logs"),
    AIRQUALITYREPORTS("Air Quality Reports"),
    ATMOSPHERICDATA("Atmospheric Data"),
    AUDIOLOGS("Audio Logs"),
    AXCOMBATDATA("AX Combat Data"),
    AXCOMBATLOGS("AX Combat Logs"),
    BALLISTICSDATA("Ballistics Data"),
    BIOMETRICDATA("Biometric Data"),
    BLACKLISTDATA("Blacklist Data"),
    BLOODTESTRESULTS("Blood Test Results"),
    CAMPAIGNPLANS("Campaign Plans"),
    CATMEDIA("Cat Media"),
    CENSUSDATA("Census Data"),
    CHEMICALEXPERIMENTDATA("Chemical Experiment Data"),
    CHEMICALFORMULAE("Chemical Formulae"),
    CHEMICALINVENTORY("Chemical Inventory"),
    CHEMICALPATENTS("Chemical Patents"),
    CLASSICENTERTAINMENT("Classic Entertainment"),
    COCKTAILRECIPES("Cocktail Recipes"),
    COMBATANTPERFORMANCE("Combatant Performance"),
    COMBATTRAININGMATERIAL("Combat Training Material"),
    CONFLICTHISTORY("Conflict History"),
    CULINARYRECIPES("Culinary Recipes"),
    DIGITALDESIGNS("Digital Designs"),
    DUTYROTA("Duty Rota"),
    EMPLOYEEDIRECTORY("Employee Directory"),
    EMPLOYEEEXPENSES("Employee Expenses"),
    EMPLOYEEGENETICDATA("Employee Genetic Data"),
    EMPLOYMENTHISTORY("Employment History"),
    EVACUATIONPROTOCOLS("Evacuation Protocols"),
    EXPLORATIONJOURNALS("Exploration Journals"),
    EXTRACTIONYIELDDATA("Extraction Yield Data"),
    FACTIONASSOCIATES("Faction Associates"),
    FACTIONDONATORLIST("Faction Donator List"),
    FACTIONNEWS("Faction News"),
    FINANCIALPROJECTIONS("Financial Projections"),
    FLEETREGISTRY("Fleet Registry"),
    GENESEQUENCINGDATA("Gene Sequencing Data"),
    GENETICRESEARCH("Genetic Research"),
    GEOGRAPHICALDATA("Geographical Data"),
    GEOLOGICALDATA("Geological Data"),
    HYDROPONICDATA("Hydroponic Data"),
    INCIDENTLOGS("Incident Logs"),
    INFLUENCEPROJECTIONS("Influence Projections"),
    INTERNALCORRESPONDENCE("Internal Correspondence"),
    INTERROGATIONRECORDINGS("Interrogation Recordings"),
    INTERVIEWRECORDINGS("Interview Recordings"),
    JOBAPPLICATIONS("Job Applications"),
    LITERARYFICTION("Literary Fiction"),
    MAINTENANCELOGS("Maintenance Logs"),
    MANUFACTURINGINSTRUCTIONS("Manufacturing Instructions"),
    MEDICALRECORDS("Medical Records"),
    MEDICALTRIALRECORDS("Clinical Trial Records"),
    MEETINGMINUTES("Meeting Minutes"),
    MINERALANALYTICS("Mineral Analytics"),
    MINERALSURVEY("Mineral Survey"),
    MININGANALYTICS("Mining Analytics"),
    MULTIMEDIAENTERTAINMENT("Multimedia Entertainment"),
    NETWORKACCESSHISTORY("Network Access History"),
    NETWORKSECURITYPROTOCOLS("Network Security Protocols"),
    NEXTOFKINRECORDS("Next of Kin Records"),
    NOCDATA("NOC Data"),
    OPERATIONALMANUAL("Operational Manual"),
    OPINIONPOLLS("Opinion Polls"),
    PATIENTHISTORY("Patient History"),
    PATROLROUTES("Patrol Routes"),
    PAYROLLINFORMATION("Payroll Information"),
    PERSONALLOGS("Personal Logs"),
    PHARMACEUTICALPATENTS("Pharmaceutical Patents"),
    PLANTGROWTHCHARTS("Plant Growth Charts"),
    PHOTOALBUMS("Photo Albums"),
    POLITICALAFFILIATIONS("Political Affiliations"),
    PRISONERLOGS("Prisoner Logs"),
    PRODUCTIONREPORTS("Production Reports"),
    PRODUCTIONSCHEDULE("Production Schedule"),
    PROPAGANDA("Propaganda"),
    PURCHASERECORDS("Purchase Records"),
    PURCHASEREQUESTS("Purchase Requests"),
    RADIOACTIVITYDATA("Radioactivity Data"),
    REACTOROUTPUTREVIEW("Reactor Output Review"),
    RECYCLINGLOGS("Recycling Logs"),
    RESIDENTIALDIRECTORY("Residential Directory"),
    RISKASSESSMENTS("Risk Assessments"),
    SALESRECORDS("Sales Records"),
    SECURITYEXPENSES("Security Expenses"),
    SECURITYPLANS("Security Plans"),
    SEEDGENEOLOGY("Seed Geneology"),
    SETTLEMENTASSAULTPLANS("Settlement Assault Plans"),
    SETTLEMENTDEFENCEPLANS("Settlement Defence Plans"),
    SHAREHOLDERINFORMATION("Shareholder Information"),
    SMEARCAMPAIGNPLANS("Smear Campaign Plans"),
    SPECTRALANALYSISDATA("Spectral Analysis Data"),
    SPYWARE("Spyware"),
    STELLARACTIVITYLOGS("Stellar Activity Logs"),
    SURVEILLEANCELOGS("Surveillance Logs"),
    TACTICALPLANS("Tactical Plans"),
    TAXRECORDS("Tax Records"),
    TOPOGRAPHICALSURVEYS("Topographical Surveys"),
    TRAVELPERMITS("Travel Permits"),
    TROOPDEPLOYMENTRECORDS("Troop Deployment Records"),
    UNIONMEMBERSHIP("Union Membership"),
    VACCINATIONRECORDS("Vaccination Records"),
    VACCINERESEARCH("Vaccine Research"),
    VIPSECURITYDATA("VIP Security Data"),
    VIROLOGYDATA("Virology Data"),
    VISITORREGISTER("Visitor Register"),
    WEAPONINVENTORY("Weapon Inventory"),
    WEAPONTESTDATA("Weapon Test Data"),
    XENODEFENCEPROTOCOLS("Xeno-Defence Protocols"),
    UNKNOWN("UNKNOWN");

    String name;

    private Data(final String name) {
        this.name = name;
    }

    public static Data forName(final String name) {
        try {
            return Data.valueOf(name.toUpperCase());
        } catch (final IllegalArgumentException ex) {
            return Data.UNKNOWN;
        }
    }

    public String friendlyName() {
        return this.name;
    }
}