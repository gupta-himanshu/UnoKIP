var chart;
$(document).ready(function() {
    
/* PIE CHART THEME */
Highcharts.theme = {
   /* LINE/BAR/COLUMN/SLICE COLORS - only used for slices for Plex, if we add multiple data sets in future releases, these colors will work with the rendering of other sets */
   colors: ['#395C9B', '#923532', '#7B972E', '#6A538D', '#3B83A1', '#CB7221', '#F2E200'],
    
   /* CHART TITLE */
   title: {
      style: {
         color: '#000',
         font: 'bold 16px "Lucida Grande", Helvetica, Arial, sans-serif'
      }
   },

   /* CHART SUBTITLE */
   subtitle: {
      style: {
         color: '#666666',
         font: 'bold 12px "Lucida Grande", Helvetica, Arial, sans-serif'
      }
   },
    
   /* CHART X-AXIS */
   xAxis: {
      lineColor: '#000',
      tickColor: '#000',
      labels: {
         style: {
            color: '#000',
            font: '11px "Lucida Grande", Helvetica, Arial, sans-serif'
         }
      },
      title: {
         style: {
            color: '#333',
            font: 'bold 12px "Lucida Grande", Helvetica, Arial, sans-serif'
         }
      }
   },
    
   /* CHART Y-AXIS */
   yAxis: {
      minorTickInterval: 'false', /* OPTIONAL PARAMETER - SHOWS HORIZONTAL LINES in between tick values */
      lineColor: '#000',
      lineWidth: 1,
      tickWidth: 1,
      tickColor: '#000',
      labels: {
         style: {
            color: '#000',
            font: '11px "Lucida Grande", Helvetica, Arial, sans-serif'
         }
      },
      title: {
         style: {
            color: '#333',
            font: 'bold 12px "Lucida Grande", Helvetica, Arial, sans-serif'
         }
      }
   },
    
   /* LINE CHART COLORS */
   plotOptions: {
       line: {
           lineWidth: 3,
           shadow: false,
           marker: {
                fillColor: '#fff', /* LINE POINT COLOR */
                lineWidth: 2,
                radius: 4,
                symbol: 'circle', /* "circle", "square", "diamond", "triangle" and "triangle-down" */
                lineColor: null // inherit from above defined colors
           }
       },
       column: {
          cursor: 'pointer',
           borderColor: '#333',
           borderWidth: 1,
           shadow: false
       },
       bar: {
          cursor: 'pointer',
          borderColor: '#333',
          borderWidth: 1,
          shadow: false
       },
       pie: {
          cursor: 'pointer',
          borderColor: '#666',
          borderWidth: 1,
          shadow: false
       }
   }    
}; 
    
// Apply the theme
var highchartsOptions = Highcharts.setOptions(Highcharts.theme);        
    
$('.pieContainer').highcharts({
        chart: {
            renderTo: '.pieContainer',
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: 'Browser market shares at a specific website, 2010'
        },
        subtitle: {
            text: 'test subtitle'
        },  
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Chrome 13',   20.0],
                ['IE 9',       14.0],
                ['Firefox 12',   12.0],
                ['IE 8',       12.0],
                {
                    name: 'Chrome 10',
                    y: 10.8,
                    sliced: true,
                    selected: true
                },
                ['Safari',    8.5],
                ['Opera',     6.2],
                ['Something',   5.7],          
                ['Else',     5.5],
                ['Others',   5.4]
            ]
        }]
    });
});