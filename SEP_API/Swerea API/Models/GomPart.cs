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
    
    public partial class GomPart
    {
        public int GomPartID { get; set; }
        public string FileName { get; set; }
        public int PartId { get; set; }
    
        public virtual Part Part { get; set; }
    }
}
