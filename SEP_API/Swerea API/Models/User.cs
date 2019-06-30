using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web;

namespace Swerea_API.Models
{
    public class User : IdentityUser
    {
        //public int UserID { get; set; }
        public string Firstname { get; set; }
        public string Lastname { get; set; }
        //public string UserName { get; set; }
        public int Level { get; set; }

        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager<User> manager, string authenticationType)
        {
            var userIdentity = await manager.CreateIdentityAsync(this, authenticationType);
            // Add custom user claims here

            return userIdentity;
        }
    }        
}