// Used d3.csv to load data from the CSV file that is  parsed via Java
d3.csv('/getDataAsCsv?fileName=BattedBallData.xlsx').then(function(data) {
    // Data loaded successfully
    console.log(data);

    // Set up the SVG container
    const svg = d3.select('div').append('svg')
        .attr('width', 900)
        .attr('height', 2000);

    // Set up scales
    const xScale = d3.scaleLinear()
        .domain([0, d3.max(data, d => +d.EXIT_SPEED)])
        .range([0, 800]);

    const yScale = d3.scaleLinear()
        .domain([0, d3.max(data, d => +d.LAUNCH_ANGLE)])
        .range([600, 0]);

    // Create circles for each data point
    svg.selectAll('circle')
        .data(data)
        .enter().append('circle')
        .attr('cx', d => xScale(+d.EXIT_SPEED))
        .attr('cy', d => yScale(+d.LAUNCH_ANGLE))
        .attr('r', 5) // radius of circles
        .style('fill', 'steelblue');

    // Add labels to circles
    svg.selectAll('text')
        .data(data)
        .enter().append('text')
        .attr('x', d => xScale(+d.EXIT_SPEED))
        .attr('y', d => yScale(+d.LAUNCH_ANGLE))
        .text(d => d.BATTER)
        .attr('font-size', '10px')
        .attr('text-anchor', 'middle')
        .attr('dy', -10); // adjust the label position

    // Add axes
    svg.append('g')
        .attr('transform', 'translate(0,600)')
        .call(d3.axisBottom(xScale))
        .append('text')
        .attr('x', 400)
        .attr('y', 40)
        .attr('fill', 'black')
        .text('Exit Speed');

    svg.append('g')
        .call(d3.axisLeft(yScale))
        .append('text')
        .attr('transform', 'rotate(-90)')
        .attr('y', -40)
        .attr('x', -300)
        .attr('fill', 'black')
        .text('Launch Angle');
}).catch(function(error) {
    // Handle any errors that occur during data loading
    console.error('Error loading data:', error);
});
