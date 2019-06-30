package android.app.printerapp.util.ui;

public class enums
{
    public enum typeOfData
    {
        buildData(0),buildStls(1), buildHistory(2), partTests(3), generalTest(4);

        private int value;

        // Enum Constructor
        typeOfData(int val)
        {
            value = val;
        }

        // getting the value of the type
        public int getValue()
        {
            return value;
        }
    }

    public enum bundleKeys
    {
        personalityComm("personalityComm"), controllerComm("controllerComm"), dialogDelegate("dialogDelegate"), filenameComm("filename");

        private String value;

        // Enum Constructor
        bundleKeys(String val)
        {
            value = val;
        }

        // getting the value of the type
        public String getValue()
        {
            return value;
        }
    }

    public enum printingData
    {
        buildID("Build ID"), startTime("Start Time"), endTime("End Time"), printingDate("Date"),
        operator("Operator"),typeOfmachine("Machine"),powderWeightStart("Start Powder"),powderweightEnd("End Powder"),
        weightPowderWaste("Waste Powder"),powerUsed("Used Powder"),platformMaterial("Platform Material"),platformWeight("Platform Weight"),
        printedTime("Print Time"),powderCondition("Powder Condition"),numberLayers("Layers"), dpcFactor("DPC Factor"),minExposureTime("Min Exposure"),
        comments("Comments");

        private String value;

        // Enum Constructor
        printingData(String val)
        {
            value = val;
        }

        // getting the value of the type
        public String getValue()
        {
            return value;
        }

    }

    public enum powderCondition
    {
        New(0),Used(1), Reused(2);

        private int value;

        // Enum Constructor
        powderCondition(int val)
        {
            value = val;
        }

        // getting the value of the type
        public int getValue()
        {
            return value;
        }
    }

    public enum postPrintingTests
    {
        General("General"),StressRelieving("Stress Relieving"), Hardening("Hardening"),Tampering("Tampering"), SolutionTreatment("Solution Treatment"), AgingTreatment("Aging Treatment");

        private String value;
        // Enum Constructor
        postPrintingTests(String val) {
        value = val;
        }

        // getting the value of the type
        public String getValue() {
            return value;
        }
    }

    public enum grantTypes
    {
        Password("password");

        private String value;

        // Enum Constructor
        grantTypes(String val)
        {
            value = val;
        }

        // getting the value of the type
        public String getValue()
        {
            return value;
        }
    }

    public enum qrCodeType
    {
        Build("Build"),Part("Part"), Material("Material");

        private String value;

        // Enum Constructor
        qrCodeType(String val)
        {
          value = val;
        }
        public String getValue()
        {
            return value;
        }
    }

    public enum APIExistMethodType
    {
        BuildExist("BuildExist"), PartExist("PartExist"), MaterialExists("MaterialExist");

        private String value;

        // Enum Constructor
        APIExistMethodType(String val)
        {
            value = val;
        }
        public String getValue()
        {
            return value;
        }
    }

    public enum userRoleType
    {
        SuperAdmin("SuperAdmin"), Admin("Admin"), Read("Read"), Edit("Edit");

        private String value;

        // Enum Constructor
        userRoleType(String val)
        {
            value = val;
        }
        public String getValue()
        {
            return value;
        }
    }

    public enum FileDownloadType
    {
        MagicsScreenShot(0), STLFile(1);

        int value;

        FileDownloadType(int value)
        {
            this. value = value;
        }

        int getvalue()
        {
            return value;
        }
    }

    public enum generalTests
    {
        Support("Support"),WEDM("WEDM"), WEDMComment("WEDM Comment"),Blasting("Blasting"), BlastingComment("Blasting Comment");

        private String value;
        // Enum Constructor
        generalTests(String val) {
            value = val;
        }

        // getting the value of the type
        public String getValue() {
            return value;
        }
    }

} //End of class
