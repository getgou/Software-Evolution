using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Swerea_API.Models.auth
{
    public class RoleBindingModel
    {
        [Required]
        [StringLength(256, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 2)]
        [Display(Name = "RoleName")]
        public string Name { get; set; }

    }

    public class UsersInRoleModel
    {

        public string Id { get; set; }
        public List<string> EnrolledUsers { get; set; }
        public List<string> RemovedUsers { get; set; }
    }
}