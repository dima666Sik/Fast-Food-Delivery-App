import React, { useEffect, useRef, useState } from "react";
import Chart from "chart.js/auto";

const PopularOrderTimeChart = ({ data }) => {
	const chartRef = useRef(null);

	useEffect(() => {
		if (!data || data.length === 0) return;

		const ctx = chartRef.current.getContext("2d");

		const chartData = {
			labels: Array.from({ length: 24 }, (_, i) => `${i}:00`), // Hours from 0 to 23
			datasets: [
				{
					label: "Number of orders",
					data: Array(24)
						.fill(0)
						.map((_, hour) => {
							// Calculate order count for each hour
							return data.filter(
								(order) => new Date(order.order_timestamp).getHours() === hour
							).length;
						}),
					backgroundColor: "rgba(75, 192, 192, 0.5)",
					borderColor: "rgba(75, 192, 192, 1)",
					borderWidth: 2,
					tension: 0.3,
				},
			],
		};

		const chartOptions = {
			responsive: true,
			plugins: {
				legend: {
					display: true,
				},
			},
			scales: {
				x: {
					title: {
						display: true,
						text: "Time (hours)",
					},
				},
				y: {
					title: {
						display: true,
						text: "Number of orders",
					},
				},
			},
		};

		const ordersChart = new Chart(ctx, {
			type: "line",
			data: chartData,
			options: chartOptions,
		});

		return () => {
			ordersChart.destroy(); // Cleanup
		};
	}, [data]);

	return <canvas ref={chartRef}></canvas>;
};

export default PopularOrderTimeChart;
