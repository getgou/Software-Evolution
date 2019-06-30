using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.Models
{
    public class ReturnedUser
    {
        public string id { get; set; }
        public string username { get; set; }
        public string firstname { get; set; }
        public string lastname { get; set; }
        public List<IdentityRole> roles {get;set;}    
    }                     
}