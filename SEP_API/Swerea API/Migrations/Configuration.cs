namespace Swerea_API.Migrations
{
    using Microsoft.AspNet.Identity;
    using Microsoft.AspNet.Identity.EntityFramework;
    using Models;
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<Swerea_API.Models.SEPEnteties>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(Swerea_API.Models.SEPEnteties context)
        {
            //  This method will be called after migrating to the latest version. 

            var manager = new UserManager<User>(new UserStore<User>(new SEPEnteties()));

            var roleManager = new RoleManager<IdentityRole>(new RoleStore<IdentityRole>(new SEPEnteties()));

            var user = new User()
            {
                UserName = "SuperPowerUser",        
                //Firstname = "Axel",
                //Lastname = "Ekdahl",
                Level = 1,                             
            };

            manager.Create(user, "password");

            if (roleManager.Roles.Count() == 0)
            {
                roleManager.Create(new IdentityRole { Name = "SuperAdmin" });
                roleManager.Create(new IdentityRole { Name = "Edit" });
                roleManager.Create(new IdentityRole { Name = "Read" });
            }

            var adminUser = manager.FindByName("SuperPowerUser");

            manager.AddToRoles(adminUser.Id, new string[] { "SuperAdmin" });
        }
    }
}
