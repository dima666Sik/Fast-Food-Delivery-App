import React from "react";
import { Bar } from "react-chartjs-2";
import {
	Chart as ChartJS,
	CategoryScale,
	LinearScale,
	BarElement,
	Title,
	Tooltip,
	Legend,
} from "chart.js";

ChartJS.register(
	CategoryScale,
	LinearScale,
	BarElement,
	Title,
	Tooltip,
	Legend
);

const ProductPopularityChart = ({ orders }) => {
	// Подсчет количества заказанных продуктов по имени
	const productPopularity = orders.reduce((acc, order) => {
		order.purchaseItems.forEach((item) => {
			acc[item.title] = (acc[item.title] || 0) + item.quantity;
		});
		return acc;
	}, {});

	// Данные для диаграммы
	const labels = Object.keys(productPopularity);
	const data = {
		labels,
		datasets: [
			{
				label: "Number of orders",
				data: Object.values(productPopularity),
				backgroundColor: "rgba(75, 192, 192, 0.6)",
				borderColor: "rgba(75, 192, 192, 1)",
				borderWidth: 1,
			},
		],
	};

	const options = {
		responsive: true,
		plugins: {
			legend: {
				position: "top",
			},
			title: {
				display: true,
				text: "Popularity of orders",
			},
		},
	};

	return <Bar data={data} options={options} />;
};

export default ProductPopularityChart;
