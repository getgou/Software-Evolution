using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Swerea_API.Models
{
    public class STLPrintingParameters
    {
        public string Filename { get; private set; }
        public double TotalZHeight { get; private set; }
        public int numberOfLayers { get; private set; }

        public STLPrintingParameters(string Filename, double TotalZHeight, int numberOfLayers)
        {
            this.Filename = Filename;
            this.TotalZHeight = TotalZHeight;
            this.numberOfLayers = numberOfLayers;
        }

        public override string ToString()
        {
            return "Filename is" + Filename +
                " with Total Z-Height of " +
                TotalZHeight + ".";
        }

    }
}