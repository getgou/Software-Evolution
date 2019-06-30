using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.Models
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    namespace Swerea_API.Models
    {
        public class ReturnedPartTest 
        {
            public bool? ShieldingGas { get; set; }
            public int PartTestId { get; set; }
            public Nullable<double> Temperature { get; set; }
            public Nullable<System.DateTime> TimeEvent { get; set; }
            public string Name { get; set; }
            public string Comment { get; set; }
            public int Type { get; set; }
        }
    }
}