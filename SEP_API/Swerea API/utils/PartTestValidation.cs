using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.utils
{
    public class PartTestValidation
    {
        public static bool tooManyGenTests(dynamic obj)
        {
            List<string> genTestList = new List<string>();
            foreach(var test in obj)
            {
                if(test.type == 5)
                {
                    genTestList.Add(Convert.ToString(test.name));
                }
            }
            if(genTestList.Count > 1)
            {
                return true;
            }

            return false;
        }
    }
}