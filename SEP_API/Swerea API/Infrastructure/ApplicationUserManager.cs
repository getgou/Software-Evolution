﻿using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin;
using Swerea_API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.Infrastructure
{
    public class ApplicationUserManager : UserManager<User>
    {
        public ApplicationUserManager(IUserStore<User> store)
            : base(store)
        {
        }

        public static ApplicationUserManager Create(IdentityFactoryOptions<ApplicationUserManager> options, IOwinContext context)
        {
            var appDbContext = context.Get<SEPEnteties>();
            var appUserManager = new ApplicationUserManager(new UserStore<User>(appDbContext));

            return appUserManager;
        }
    }
}