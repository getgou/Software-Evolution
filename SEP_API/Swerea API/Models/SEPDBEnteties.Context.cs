﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Swerea_API.Models
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    
    public partial class SEPDBEntities : DbContext
    {
        public SEPDBEntities()
            : base("name=SEPDBEntities1")
        {
        }
    
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            throw new UnintentionalCodeFirstException();
        }
    
        public virtual DbSet<C__MigrationHistory> C__MigrationHistory { get; set; }
        public virtual DbSet<AspNetRoles> AspNetRoles { get; set; }
        public virtual DbSet<AspNetUserClaims> AspNetUserClaims { get; set; }
        public virtual DbSet<AspNetUserLogins> AspNetUserLogins { get; set; }
        public virtual DbSet<AspNetUsers> AspNetUsers { get; set; }
        public virtual DbSet<Build> Build { get; set; }
        public virtual DbSet<GeneralTest> GeneralTest { get; set; }
        public virtual DbSet<GomBuild> GomBuild { get; set; }
        public virtual DbSet<GomPart> GomPart { get; set; }
        public virtual DbSet<Magic> Magic { get; set; }
        public virtual DbSet<Material> Material { get; set; }
        public virtual DbSet<MaterialPdf> MaterialPdf { get; set; }
        public virtual DbSet<Part> Part { get; set; }
        public virtual DbSet<PartTest> PartTest { get; set; }
        public virtual DbSet<PartTestDetails> PartTestDetails { get; set; }
        public virtual DbSet<PrintingInfo> PrintingInfo { get; set; }
        public virtual DbSet<SLM> SLM { get; set; }
        public virtual DbSet<StressRelieveingTest> StressRelieveingTest { get; set; }
        public virtual DbSet<sysdiagrams> sysdiagrams { get; set; }
        public virtual DbSet<Users> Users { get; set; }
        public virtual DbSet<VariableParameters> VariableParameters { get; set; }
        public virtual DbSet<EstimatedValues> EstimatedValues { get; set; }
        public virtual DbSet<ConstantParameters> ConstantParameters { get; set; }
    }
}
