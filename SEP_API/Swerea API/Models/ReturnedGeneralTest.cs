using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.Models
{
    public class ReturnedGeneralTest
    {             
        public bool? SupportRemoval { get; set; }
        public bool? WEDM { get; set; }
        public string wedmComment { get; set; }
        public int GeneralId { get; set; }
        public bool? Blasting { get; set; }
        public string BlastingComments { get; set; }
        public int Type { get; set; }
    }
}
