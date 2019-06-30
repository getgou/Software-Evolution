using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.Models
{
    public class SimpleBuild
    {
        public int BuildID { get; set; }
        public string QR_code { get; set; }
        public Nullable<int> MaterialId { get; set; }
        public Nullable<int> Status { get; set; }
    }
}