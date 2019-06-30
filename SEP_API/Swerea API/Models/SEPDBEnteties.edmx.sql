
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 12/07/2018 19:43:08
-- Generated from EDMX file: E:\SEP\2018Group13\SEP_API\Swerea API\Models\SEPDBEnteties.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [SEPDB];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_dbo_AspNetUserClaims_dbo_AspNetUsers_UserId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[AspNetUserClaims] DROP CONSTRAINT [FK_dbo_AspNetUserClaims_dbo_AspNetUsers_UserId];
GO
IF OBJECT_ID(N'[dbo].[FK_dbo_AspNetUserLogins_dbo_AspNetUsers_UserId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[AspNetUserLogins] DROP CONSTRAINT [FK_dbo_AspNetUserLogins_dbo_AspNetUsers_UserId];
GO
IF OBJECT_ID(N'[dbo].[FK_Gom_Build_Build]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[GomBuild] DROP CONSTRAINT [FK_Gom_Build_Build];
GO
IF OBJECT_ID(N'[dbo].[FK_Magic_Magic]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Magic] DROP CONSTRAINT [FK_Magic_Magic];
GO
IF OBJECT_ID(N'[dbo].[FK_Part_Build]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Part] DROP CONSTRAINT [FK_Part_Build];
GO
IF OBJECT_ID(N'[dbo].[FK_PrintingInfo_Build]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[PrintingInfo] DROP CONSTRAINT [FK_PrintingInfo_Build];
GO
IF OBJECT_ID(N'[dbo].[FK_SLM_SLM]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[SLM] DROP CONSTRAINT [FK_SLM_SLM];
GO
IF OBJECT_ID(N'[dbo].[FK_Gom_part_Part]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[GomPart] DROP CONSTRAINT [FK_Gom_part_Part];
GO
IF OBJECT_ID(N'[dbo].[FK_Material_Pdf_Material]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[MaterialPdf] DROP CONSTRAINT [FK_Material_Pdf_Material];
GO
IF OBJECT_ID(N'[dbo].[FK_part_test_details_Part]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[PartTestDetails] DROP CONSTRAINT [FK_part_test_details_Part];
GO
IF OBJECT_ID(N'[dbo].[FK_part_test_details_part_test_details]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[PartTestDetails] DROP CONSTRAINT [FK_part_test_details_part_test_details];
GO
IF OBJECT_ID(N'[dbo].[FK_StressRelieveingTest_PartTest]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[StressRelieveingTest] DROP CONSTRAINT [FK_StressRelieveingTest_PartTest];
GO
IF OBJECT_ID(N'[dbo].[FK_AspNetUserRoles_AspNetRoles]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[AspNetUserRoles] DROP CONSTRAINT [FK_AspNetUserRoles_AspNetRoles];
GO
IF OBJECT_ID(N'[dbo].[FK_AspNetUserRoles_AspNetUsers]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[AspNetUserRoles] DROP CONSTRAINT [FK_AspNetUserRoles_AspNetUsers];
GO
IF OBJECT_ID(N'[dbo].[FK_BuildEstimatedValues]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[EstimatedValues] DROP CONSTRAINT [FK_BuildEstimatedValues];
GO
IF OBJECT_ID(N'[dbo].[FK_BuildVariableParameters]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[VariableParameters] DROP CONSTRAINT [FK_BuildVariableParameters];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[C__MigrationHistory]', 'U') IS NOT NULL
    DROP TABLE [dbo].[C__MigrationHistory];
GO
IF OBJECT_ID(N'[dbo].[AspNetRoles]', 'U') IS NOT NULL
    DROP TABLE [dbo].[AspNetRoles];
GO
IF OBJECT_ID(N'[dbo].[AspNetUserClaims]', 'U') IS NOT NULL
    DROP TABLE [dbo].[AspNetUserClaims];
GO
IF OBJECT_ID(N'[dbo].[AspNetUserLogins]', 'U') IS NOT NULL
    DROP TABLE [dbo].[AspNetUserLogins];
GO
IF OBJECT_ID(N'[dbo].[AspNetUsers]', 'U') IS NOT NULL
    DROP TABLE [dbo].[AspNetUsers];
GO
IF OBJECT_ID(N'[dbo].[Build]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Build];
GO
IF OBJECT_ID(N'[dbo].[GeneralTest]', 'U') IS NOT NULL
    DROP TABLE [dbo].[GeneralTest];
GO
IF OBJECT_ID(N'[dbo].[GomBuild]', 'U') IS NOT NULL
    DROP TABLE [dbo].[GomBuild];
GO
IF OBJECT_ID(N'[dbo].[GomPart]', 'U') IS NOT NULL
    DROP TABLE [dbo].[GomPart];
GO
IF OBJECT_ID(N'[dbo].[Magic]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Magic];
GO
IF OBJECT_ID(N'[dbo].[Material]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Material];
GO
IF OBJECT_ID(N'[dbo].[MaterialPdf]', 'U') IS NOT NULL
    DROP TABLE [dbo].[MaterialPdf];
GO
IF OBJECT_ID(N'[dbo].[Part]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Part];
GO
IF OBJECT_ID(N'[dbo].[PartTest]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PartTest];
GO
IF OBJECT_ID(N'[dbo].[PartTestDetails]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PartTestDetails];
GO
IF OBJECT_ID(N'[dbo].[PrintingInfo]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PrintingInfo];
GO
IF OBJECT_ID(N'[dbo].[SLM]', 'U') IS NOT NULL
    DROP TABLE [dbo].[SLM];
GO
IF OBJECT_ID(N'[dbo].[StressRelieveingTest]', 'U') IS NOT NULL
    DROP TABLE [dbo].[StressRelieveingTest];
GO
IF OBJECT_ID(N'[dbo].[sysdiagrams]', 'U') IS NOT NULL
    DROP TABLE [dbo].[sysdiagrams];
GO
IF OBJECT_ID(N'[dbo].[Users]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Users];
GO
IF OBJECT_ID(N'[dbo].[VariableParameters]', 'U') IS NOT NULL
    DROP TABLE [dbo].[VariableParameters];
GO
IF OBJECT_ID(N'[dbo].[EstimatedValues]', 'U') IS NOT NULL
    DROP TABLE [dbo].[EstimatedValues];
GO
IF OBJECT_ID(N'[dbo].[ConstantParameters]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ConstantParameters];
GO
IF OBJECT_ID(N'[dbo].[AspNetUserRoles]', 'U') IS NOT NULL
    DROP TABLE [dbo].[AspNetUserRoles];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'C__MigrationHistory'
CREATE TABLE [dbo].[C__MigrationHistory] (
    [MigrationId] nvarchar(150)  NOT NULL,
    [ContextKey] nvarchar(300)  NOT NULL,
    [Model] varbinary(max)  NOT NULL,
    [ProductVersion] nvarchar(32)  NOT NULL
);
GO

-- Creating table 'AspNetRoles'
CREATE TABLE [dbo].[AspNetRoles] (
    [Id] nvarchar(128)  NOT NULL,
    [Name] nvarchar(256)  NOT NULL
);
GO

-- Creating table 'AspNetUserClaims'
CREATE TABLE [dbo].[AspNetUserClaims] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [UserId] nvarchar(128)  NOT NULL,
    [ClaimType] nvarchar(max)  NULL,
    [ClaimValue] nvarchar(max)  NULL
);
GO

-- Creating table 'AspNetUserLogins'
CREATE TABLE [dbo].[AspNetUserLogins] (
    [LoginProvider] nvarchar(128)  NOT NULL,
    [ProviderKey] nvarchar(128)  NOT NULL,
    [UserId] nvarchar(128)  NOT NULL
);
GO

-- Creating table 'AspNetUsers'
CREATE TABLE [dbo].[AspNetUsers] (
    [Id] nvarchar(128)  NOT NULL,
    [Firstname] nvarchar(max)  NULL,
    [Lastname] nvarchar(max)  NULL,
    [Level] int  NOT NULL,
    [Email] nvarchar(256)  NULL,
    [EmailConfirmed] bit  NOT NULL,
    [PasswordHash] nvarchar(max)  NULL,
    [SecurityStamp] nvarchar(max)  NULL,
    [PhoneNumber] nvarchar(max)  NULL,
    [PhoneNumberConfirmed] bit  NOT NULL,
    [TwoFactorEnabled] bit  NOT NULL,
    [LockoutEndDateUtc] datetime  NULL,
    [LockoutEnabled] bit  NOT NULL,
    [AccessFailedCount] int  NOT NULL,
    [UserName] nvarchar(256)  NOT NULL
);
GO

-- Creating table 'Build'
CREATE TABLE [dbo].[Build] (
    [BuildID] int IDENTITY(1,1) NOT NULL,
    [QR_code] nvarchar(50)  NULL,
    [MaterialId] int  NULL,
    [Status] int  NULL
);
GO

-- Creating table 'GeneralTest'
CREATE TABLE [dbo].[GeneralTest] (
    [SupportRemoval] bit  NULL,
    [WEDM] bit  NULL,
    [WEDMComments] nvarchar(max)  NULL,
    [Blasting] bit  NULL,
    [BlastingComments] nvarchar(50)  NULL,
    [GeneralID] int IDENTITY(1,1) NOT NULL,
    [QRCode] nvarchar(50)  NULL,
    [PartId] int  NULL
);
GO

-- Creating table 'GomBuild'
CREATE TABLE [dbo].[GomBuild] (
    [GomBuildID] int IDENTITY(1,1) NOT NULL,
    [FileName] nvarchar(50)  NOT NULL,
    [BuildId] int  NOT NULL
);
GO

-- Creating table 'GomPart'
CREATE TABLE [dbo].[GomPart] (
    [GomPartID] int IDENTITY(1,1) NOT NULL,
    [FileName] nvarchar(50)  NULL,
    [PartId] int  NOT NULL
);
GO

-- Creating table 'Magic'
CREATE TABLE [dbo].[Magic] (
    [MagicID] int IDENTITY(1,1) NOT NULL,
    [FileName] nvarchar(50)  NOT NULL,
    [MagicScreenshot] nvarchar(50)  NULL,
    [BuildId] int  NOT NULL
);
GO

-- Creating table 'Material'
CREATE TABLE [dbo].[Material] (
    [MaterialID] int IDENTITY(1,1) NOT NULL,
    [QR_code] nvarchar(50)  NULL
);
GO

-- Creating table 'MaterialPdf'
CREATE TABLE [dbo].[MaterialPdf] (
    [MaterialPdfID] int IDENTITY(1,1) NOT NULL,
    [FileName] nvarchar(50)  NOT NULL,
    [MaterialId] int  NULL
);
GO

-- Creating table 'Part'
CREATE TABLE [dbo].[Part] (
    [PartID] int IDENTITY(1,1) NOT NULL,
    [StlFileName] nvarchar(50)  NULL,
    [PrtFileName] nvarchar(50)  NULL,
    [QR_code] nvarchar(50)  NULL,
    [BuildId] int  NOT NULL,
    [MagicID] nvarchar(50)  NULL
);
GO

-- Creating table 'PartTest'
CREATE TABLE [dbo].[PartTest] (
    [PartTestID] int IDENTITY(1,1) NOT NULL,
    [Temperature] float  NULL,
    [TimeEvent] datetime  NULL,
    [Name] nvarchar(50)  NULL,
    [Comment] nvarchar(50)  NULL,
    [Type] int  NOT NULL
);
GO

-- Creating table 'PartTestDetails'
CREATE TABLE [dbo].[PartTestDetails] (
    [PartTestDetailsID] int IDENTITY(1,1) NOT NULL,
    [PartId] int  NULL,
    [PartTestId] int  NULL
);
GO

-- Creating table 'PrintingInfo'
CREATE TABLE [dbo].[PrintingInfo] (
    [PrintingInfoID] int IDENTITY(1,1) NOT NULL,
    [BuildStatus] nvarchar(150)  NULL,
    [StartTime] datetime  NULL,
    [EndTime] datetime  NULL,
    [PrintingDate] datetime  NULL,
    [Operator] nvarchar(200)  NULL,
    [TypeOfmachine] nvarchar(200)  NULL,
    [PowderWeightStart] float  NULL,
    [PowderweightEnd] float  NULL,
    [WeightPowderWaste] float  NULL,
    [PowerUsed] float  NULL,
    [PlatformMaterial] nvarchar(250)  NULL,
    [PlatformWeight] float  NULL,
    [PrintedTime] nvarchar(30)  NULL,
    [PowderCondition] nvarchar(30)  NULL,
    [NumberLayers] int  NULL,
    [DPCFactor] float  NULL,
    [MinExposureTime] nvarchar(30)  NULL,
    [Comments] nchar(500)  NULL,
    [BuildId] int  NOT NULL
);
GO

-- Creating table 'SLM'
CREATE TABLE [dbo].[SLM] (
    [SLMID] int IDENTITY(1,1) NOT NULL,
    [FileName] nvarchar(50)  NOT NULL,
    [BuildId] int  NOT NULL
);
GO

-- Creating table 'StressRelieveingTest'
CREATE TABLE [dbo].[StressRelieveingTest] (
    [StressRelieveingTestID] int IDENTITY(1,1) NOT NULL,
    [ShieldingGas] bit  NOT NULL,
    [PartTestId] int  NOT NULL
);
GO

-- Creating table 'sysdiagrams'
CREATE TABLE [dbo].[sysdiagrams] (
    [name] nvarchar(128)  NOT NULL,
    [principal_id] int  NOT NULL,
    [diagram_id] int IDENTITY(1,1) NOT NULL,
    [version] int  NULL,
    [definition] varbinary(max)  NULL
);
GO

-- Creating table 'Users'
CREATE TABLE [dbo].[Users] (
    [UserID] int IDENTITY(1,1) NOT NULL,
    [Firstname] nvarchar(max)  NULL,
    [Lastname] nvarchar(max)  NULL,
    [Username] nvarchar(max)  NULL
);
GO

-- Creating table 'VariableParameters'
CREATE TABLE [dbo].[VariableParameters] (
    [ParamID] int IDENTITY(1,1) NOT NULL,
    [MaterialType] nvarchar(max)  NOT NULL,
    [Volume] float  NOT NULL,
    [Weight] float  NOT NULL,
    [ZHeight] float  NOT NULL,
    [NumberOfLayers] int  NOT NULL,
    [UnitOfLength] nvarchar(max)  NOT NULL,
    [InfillPercentage] float  NOT NULL,
    [FileName] nvarchar(max)  NOT NULL,
    [BuildId] int  NOT NULL
);
GO

-- Creating table 'EstimatedValues'
CREATE TABLE [dbo].[EstimatedValues] (
    [ValueID] int IDENTITY(1,1) NOT NULL,
    [TotalPowderUsed] float  NOT NULL,
    [TotalPrintingTime] float  NOT NULL,
    [TotalCost] float  NOT NULL,
    [BuildId] int  NOT NULL
);
GO

-- Creating table 'ConstantParameters'
CREATE TABLE [dbo].[ConstantParameters] (
    [ConstID] int IDENTITY(1,1) NOT NULL,
    [PrinterCost] float  NOT NULL,
    [MaterialCost] float  NOT NULL,
    [LabourCost] float  NOT NULL,
    [PreProcessingTime] float  NOT NULL,
    [SetupTime] float  NOT NULL,
    [PowderHandlingTime] float  NOT NULL,
    [JobStartTime] float  NOT NULL,
    [BuildRemovalTime] float  NOT NULL,
    [PartRemovalTime] float  NOT NULL,
    [PostProcessingTime] float  NOT NULL,
    [MachineCleaningTime] float  NOT NULL
);
GO

-- Creating table 'AspNetUserRoles'
CREATE TABLE [dbo].[AspNetUserRoles] (
    [AspNetRoles_Id] nvarchar(128)  NOT NULL,
    [AspNetUsers_Id] nvarchar(128)  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [MigrationId], [ContextKey] in table 'C__MigrationHistory'
ALTER TABLE [dbo].[C__MigrationHistory]
ADD CONSTRAINT [PK_C__MigrationHistory]
    PRIMARY KEY CLUSTERED ([MigrationId], [ContextKey] ASC);
GO

-- Creating primary key on [Id] in table 'AspNetRoles'
ALTER TABLE [dbo].[AspNetRoles]
ADD CONSTRAINT [PK_AspNetRoles]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'AspNetUserClaims'
ALTER TABLE [dbo].[AspNetUserClaims]
ADD CONSTRAINT [PK_AspNetUserClaims]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [LoginProvider], [ProviderKey], [UserId] in table 'AspNetUserLogins'
ALTER TABLE [dbo].[AspNetUserLogins]
ADD CONSTRAINT [PK_AspNetUserLogins]
    PRIMARY KEY CLUSTERED ([LoginProvider], [ProviderKey], [UserId] ASC);
GO

-- Creating primary key on [Id] in table 'AspNetUsers'
ALTER TABLE [dbo].[AspNetUsers]
ADD CONSTRAINT [PK_AspNetUsers]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [BuildID] in table 'Build'
ALTER TABLE [dbo].[Build]
ADD CONSTRAINT [PK_Build]
    PRIMARY KEY CLUSTERED ([BuildID] ASC);
GO

-- Creating primary key on [GeneralID] in table 'GeneralTest'
ALTER TABLE [dbo].[GeneralTest]
ADD CONSTRAINT [PK_GeneralTest]
    PRIMARY KEY CLUSTERED ([GeneralID] ASC);
GO

-- Creating primary key on [GomBuildID] in table 'GomBuild'
ALTER TABLE [dbo].[GomBuild]
ADD CONSTRAINT [PK_GomBuild]
    PRIMARY KEY CLUSTERED ([GomBuildID] ASC);
GO

-- Creating primary key on [GomPartID] in table 'GomPart'
ALTER TABLE [dbo].[GomPart]
ADD CONSTRAINT [PK_GomPart]
    PRIMARY KEY CLUSTERED ([GomPartID] ASC);
GO

-- Creating primary key on [MagicID] in table 'Magic'
ALTER TABLE [dbo].[Magic]
ADD CONSTRAINT [PK_Magic]
    PRIMARY KEY CLUSTERED ([MagicID] ASC);
GO

-- Creating primary key on [MaterialID] in table 'Material'
ALTER TABLE [dbo].[Material]
ADD CONSTRAINT [PK_Material]
    PRIMARY KEY CLUSTERED ([MaterialID] ASC);
GO

-- Creating primary key on [MaterialPdfID] in table 'MaterialPdf'
ALTER TABLE [dbo].[MaterialPdf]
ADD CONSTRAINT [PK_MaterialPdf]
    PRIMARY KEY CLUSTERED ([MaterialPdfID] ASC);
GO

-- Creating primary key on [PartID] in table 'Part'
ALTER TABLE [dbo].[Part]
ADD CONSTRAINT [PK_Part]
    PRIMARY KEY CLUSTERED ([PartID] ASC);
GO

-- Creating primary key on [PartTestID] in table 'PartTest'
ALTER TABLE [dbo].[PartTest]
ADD CONSTRAINT [PK_PartTest]
    PRIMARY KEY CLUSTERED ([PartTestID] ASC);
GO

-- Creating primary key on [PartTestDetailsID] in table 'PartTestDetails'
ALTER TABLE [dbo].[PartTestDetails]
ADD CONSTRAINT [PK_PartTestDetails]
    PRIMARY KEY CLUSTERED ([PartTestDetailsID] ASC);
GO

-- Creating primary key on [PrintingInfoID] in table 'PrintingInfo'
ALTER TABLE [dbo].[PrintingInfo]
ADD CONSTRAINT [PK_PrintingInfo]
    PRIMARY KEY CLUSTERED ([PrintingInfoID] ASC);
GO

-- Creating primary key on [SLMID] in table 'SLM'
ALTER TABLE [dbo].[SLM]
ADD CONSTRAINT [PK_SLM]
    PRIMARY KEY CLUSTERED ([SLMID] ASC);
GO

-- Creating primary key on [StressRelieveingTestID] in table 'StressRelieveingTest'
ALTER TABLE [dbo].[StressRelieveingTest]
ADD CONSTRAINT [PK_StressRelieveingTest]
    PRIMARY KEY CLUSTERED ([StressRelieveingTestID] ASC);
GO

-- Creating primary key on [diagram_id] in table 'sysdiagrams'
ALTER TABLE [dbo].[sysdiagrams]
ADD CONSTRAINT [PK_sysdiagrams]
    PRIMARY KEY CLUSTERED ([diagram_id] ASC);
GO

-- Creating primary key on [UserID] in table 'Users'
ALTER TABLE [dbo].[Users]
ADD CONSTRAINT [PK_Users]
    PRIMARY KEY CLUSTERED ([UserID] ASC);
GO

-- Creating primary key on [ParamID] in table 'VariableParameters'
ALTER TABLE [dbo].[VariableParameters]
ADD CONSTRAINT [PK_VariableParameters]
    PRIMARY KEY CLUSTERED ([ParamID] ASC);
GO

-- Creating primary key on [ValueID] in table 'EstimatedValues'
ALTER TABLE [dbo].[EstimatedValues]
ADD CONSTRAINT [PK_EstimatedValues]
    PRIMARY KEY CLUSTERED ([ValueID] ASC);
GO

-- Creating primary key on [ConstID] in table 'ConstantParameters'
ALTER TABLE [dbo].[ConstantParameters]
ADD CONSTRAINT [PK_ConstantParameters]
    PRIMARY KEY CLUSTERED ([ConstID] ASC);
GO

-- Creating primary key on [AspNetRoles_Id], [AspNetUsers_Id] in table 'AspNetUserRoles'
ALTER TABLE [dbo].[AspNetUserRoles]
ADD CONSTRAINT [PK_AspNetUserRoles]
    PRIMARY KEY CLUSTERED ([AspNetRoles_Id], [AspNetUsers_Id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [UserId] in table 'AspNetUserClaims'
ALTER TABLE [dbo].[AspNetUserClaims]
ADD CONSTRAINT [FK_dbo_AspNetUserClaims_dbo_AspNetUsers_UserId]
    FOREIGN KEY ([UserId])
    REFERENCES [dbo].[AspNetUsers]
        ([Id])
    ON DELETE CASCADE ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_dbo_AspNetUserClaims_dbo_AspNetUsers_UserId'
CREATE INDEX [IX_FK_dbo_AspNetUserClaims_dbo_AspNetUsers_UserId]
ON [dbo].[AspNetUserClaims]
    ([UserId]);
GO

-- Creating foreign key on [UserId] in table 'AspNetUserLogins'
ALTER TABLE [dbo].[AspNetUserLogins]
ADD CONSTRAINT [FK_dbo_AspNetUserLogins_dbo_AspNetUsers_UserId]
    FOREIGN KEY ([UserId])
    REFERENCES [dbo].[AspNetUsers]
        ([Id])
    ON DELETE CASCADE ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_dbo_AspNetUserLogins_dbo_AspNetUsers_UserId'
CREATE INDEX [IX_FK_dbo_AspNetUserLogins_dbo_AspNetUsers_UserId]
ON [dbo].[AspNetUserLogins]
    ([UserId]);
GO

-- Creating foreign key on [BuildId] in table 'GomBuild'
ALTER TABLE [dbo].[GomBuild]
ADD CONSTRAINT [FK_Gom_Build_Build]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Gom_Build_Build'
CREATE INDEX [IX_FK_Gom_Build_Build]
ON [dbo].[GomBuild]
    ([BuildId]);
GO

-- Creating foreign key on [BuildId] in table 'Magic'
ALTER TABLE [dbo].[Magic]
ADD CONSTRAINT [FK_Magic_Magic]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Magic_Magic'
CREATE INDEX [IX_FK_Magic_Magic]
ON [dbo].[Magic]
    ([BuildId]);
GO

-- Creating foreign key on [BuildId] in table 'Part'
ALTER TABLE [dbo].[Part]
ADD CONSTRAINT [FK_Part_Build]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Part_Build'
CREATE INDEX [IX_FK_Part_Build]
ON [dbo].[Part]
    ([BuildId]);
GO

-- Creating foreign key on [BuildId] in table 'PrintingInfo'
ALTER TABLE [dbo].[PrintingInfo]
ADD CONSTRAINT [FK_PrintingInfo_Build]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_PrintingInfo_Build'
CREATE INDEX [IX_FK_PrintingInfo_Build]
ON [dbo].[PrintingInfo]
    ([BuildId]);
GO

-- Creating foreign key on [BuildId] in table 'SLM'
ALTER TABLE [dbo].[SLM]
ADD CONSTRAINT [FK_SLM_SLM]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_SLM_SLM'
CREATE INDEX [IX_FK_SLM_SLM]
ON [dbo].[SLM]
    ([BuildId]);
GO

-- Creating foreign key on [PartId] in table 'GomPart'
ALTER TABLE [dbo].[GomPart]
ADD CONSTRAINT [FK_Gom_part_Part]
    FOREIGN KEY ([PartId])
    REFERENCES [dbo].[Part]
        ([PartID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Gom_part_Part'
CREATE INDEX [IX_FK_Gom_part_Part]
ON [dbo].[GomPart]
    ([PartId]);
GO

-- Creating foreign key on [MaterialId] in table 'MaterialPdf'
ALTER TABLE [dbo].[MaterialPdf]
ADD CONSTRAINT [FK_Material_Pdf_Material]
    FOREIGN KEY ([MaterialId])
    REFERENCES [dbo].[Material]
        ([MaterialID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Material_Pdf_Material'
CREATE INDEX [IX_FK_Material_Pdf_Material]
ON [dbo].[MaterialPdf]
    ([MaterialId]);
GO

-- Creating foreign key on [PartId] in table 'PartTestDetails'
ALTER TABLE [dbo].[PartTestDetails]
ADD CONSTRAINT [FK_part_test_details_Part]
    FOREIGN KEY ([PartId])
    REFERENCES [dbo].[Part]
        ([PartID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_part_test_details_Part'
CREATE INDEX [IX_FK_part_test_details_Part]
ON [dbo].[PartTestDetails]
    ([PartId]);
GO

-- Creating foreign key on [PartTestId] in table 'PartTestDetails'
ALTER TABLE [dbo].[PartTestDetails]
ADD CONSTRAINT [FK_part_test_details_part_test_details]
    FOREIGN KEY ([PartTestId])
    REFERENCES [dbo].[PartTest]
        ([PartTestID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_part_test_details_part_test_details'
CREATE INDEX [IX_FK_part_test_details_part_test_details]
ON [dbo].[PartTestDetails]
    ([PartTestId]);
GO

-- Creating foreign key on [PartTestId] in table 'StressRelieveingTest'
ALTER TABLE [dbo].[StressRelieveingTest]
ADD CONSTRAINT [FK_StressRelieveingTest_PartTest]
    FOREIGN KEY ([PartTestId])
    REFERENCES [dbo].[PartTest]
        ([PartTestID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_StressRelieveingTest_PartTest'
CREATE INDEX [IX_FK_StressRelieveingTest_PartTest]
ON [dbo].[StressRelieveingTest]
    ([PartTestId]);
GO

-- Creating foreign key on [AspNetRoles_Id] in table 'AspNetUserRoles'
ALTER TABLE [dbo].[AspNetUserRoles]
ADD CONSTRAINT [FK_AspNetUserRoles_AspNetRoles]
    FOREIGN KEY ([AspNetRoles_Id])
    REFERENCES [dbo].[AspNetRoles]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [AspNetUsers_Id] in table 'AspNetUserRoles'
ALTER TABLE [dbo].[AspNetUserRoles]
ADD CONSTRAINT [FK_AspNetUserRoles_AspNetUsers]
    FOREIGN KEY ([AspNetUsers_Id])
    REFERENCES [dbo].[AspNetUsers]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_AspNetUserRoles_AspNetUsers'
CREATE INDEX [IX_FK_AspNetUserRoles_AspNetUsers]
ON [dbo].[AspNetUserRoles]
    ([AspNetUsers_Id]);
GO

-- Creating foreign key on [BuildId] in table 'EstimatedValues'
ALTER TABLE [dbo].[EstimatedValues]
ADD CONSTRAINT [FK_BuildEstimatedValues]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_BuildEstimatedValues'
CREATE INDEX [IX_FK_BuildEstimatedValues]
ON [dbo].[EstimatedValues]
    ([BuildId]);
GO

-- Creating foreign key on [BuildId] in table 'VariableParameters'
ALTER TABLE [dbo].[VariableParameters]
ADD CONSTRAINT [FK_BuildVariableParameters]
    FOREIGN KEY ([BuildId])
    REFERENCES [dbo].[Build]
        ([BuildID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_BuildVariableParameters'
CREATE INDEX [IX_FK_BuildVariableParameters]
ON [dbo].[VariableParameters]
    ([BuildId]);
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------