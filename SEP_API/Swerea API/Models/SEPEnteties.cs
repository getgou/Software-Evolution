using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace Swerea_API.Models
{
    public class SEPEnteties : IdentityDbContext<User>
    {
        public SEPEnteties() : base("SEPDBAWS", throwIfV1Schema: false)
        {
            Configuration.ProxyCreationEnabled = false;
            Configuration.LazyLoadingEnabled = false;
        }

        public static SEPEnteties Create()
        {
            return new SEPEnteties();
        }

    }
}