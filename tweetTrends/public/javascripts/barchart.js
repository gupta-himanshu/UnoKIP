var data = 	[
			{"x": 0,"y": 0,"w":20,"h": 380,"color": "green"},
			{"x": 0,"y": 40,"w":200,"h": 308,"color": "#121212"},
			{"x": 0,"y": 80,"w":120,"h": 238,"color": "purple"},
			{"x": 0,"y": 0,"w":20,"h": 353,"color": "yellow"},
			{"x": 0,"y": 40,"w":200,"h": 308,"color": "red"},
			{"x": 0,"y": 120,"w":140,"h": 138,"color": "grey"}
		];
		
        var width = 420,
            barHeight = 25;

        var x = d3.scale.linear()
            .domain([0, d3.max(data)])
            .range([0, width]);

        var chart = d3.select(".chart")
            .attr("width", width)
            .attr("height", barHeight * data.length);

        var bar = chart.selectAll("g")
            .data(data)
            .enter().append("g")
            .attr("transform", function(d, i) { return "translate(0," + i * barHeight + ")"; });

        bar.append("rect")
            .attr("width",  function (d) { return d.h; })
            .attr("height", barHeight - 1)
            .style("fill", function(d) { return d.color; });
        	

        bar.append("text")
        .attr("x", function(d) { return d.h -3; })
        .attr("y", barHeight / 2)
        .attr("dy", ".35em")
        .text(function(d) { return d.color; });