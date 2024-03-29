//------------------------------------------------------------------------------
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
    using System.Collections.Generic;
    
    public partial class PartTest
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public PartTest()
        {
            this.PartTestDetails = new HashSet<PartTestDetails>();
            this.StressRelieveingTest = new HashSet<StressRelieveingTest>();
        }
    
        public int PartTestID { get; set; }
        public Nullable<double> Temperature { get; set; }
        public Nullable<System.DateTime> TimeEvent { get; set; }
        public string Name { get; set; }
        public string Comment { get; set; }
        public int Type { get; set; }
    
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<PartTestDetails> PartTestDetails { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<StressRelieveingTest> StressRelieveingTest { get; set; }
    }
}
