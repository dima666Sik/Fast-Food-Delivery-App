// all images imported from images directory
import product_01_image_01 from "../images/products/product_01.jpg";

import product_01_image_02 from "../images/products/product_01.1.jpg";
import product_01_image_03 from "../images/products/product_01.3.jpg";

import product_02_image_01 from "../images/products/product_2.1.jpg";
import product_02_image_02 from "../images/products/product_2.2.jpg";
import product_02_image_03 from "../images/products/product_2.3.jpg";

import product_03_image_01 from "../images/products/product_3.1.jpg";
import product_03_image_02 from "../images/products/product_3.2.jpg";
import product_03_image_03 from "../images/products/product_3.3.jpg";

import product_04_image_01 from "../images/products/product_4.1.jpg";
import product_04_image_02 from "../images/products/product_4.2.jpg";
import product_04_image_03 from "../images/products/product_4.3.png";

import product_05_image_01 from "../images/products/product_04.jpg";
import product_05_image_02 from "../images/products/product_08.jpg";
import product_05_image_03 from "../images/products/product_09.jpg";

import product_06_image_01 from "../images/products/sushi_1.png";
import product_06_image_02 from "../images/products/sushi_2.png";
import product_06_image_03 from "../images/products/sushi_3.png";

const products = [
	{
		id: "01",
		title: "Chicken Burger",
		price: 24.0,
		likes: 120,
		image01: product_01_image_01,
		image02: product_01_image_02,
		image03: product_01_image_03,
		category: "Burger",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque. ",
	},

	{
		id: "02",
		title: "Vegetarian Pizza",
		price: 115.0,
		likes: 130,
		image01: product_02_image_01,
		image02: product_02_image_02,
		image03: product_02_image_03,
		category: "Pizza",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "03",
		title: "Double Cheese Margherita",
		price: 110.0,
		likes: 90,
		image01: product_03_image_01,
		image02: product_03_image_02,
		image03: product_03_image_03,
		category: "Pizza",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "04",
		title: "Maxican Green Wave",
		price: 110.0,
		likes: 125,
		image01: product_04_image_01,
		image02: product_04_image_02,
		image03: product_04_image_03,
		category: "Pizza",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "05",
		title: "Cheese Burger",
		price: 24.0,
		likes: 170,
		image01: product_05_image_01,
		image02: product_05_image_02,
		image03: product_05_image_03,
		category: "Burger",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},
	{
		id: "06",
		title: "Royal Cheese Burger",
		price: 24.0,
		likes: 110,
		image01: product_01_image_01,
		image02: product_01_image_02,
		image03: product_01_image_03,
		category: "Burger",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "07",
		title: "Seafood Pizza",
		price: 115.0,
		likes: 70,
		image01: product_02_image_02,
		image02: product_02_image_01,
		image03: product_02_image_03,
		category: "Pizza",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "08",
		title: "Thin Cheese Pizza",
		price: 110.0,
		likes: 220,
		image01: product_03_image_02,
		image02: product_03_image_01,
		image03: product_03_image_03,
		category: "Pizza",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "09",
		title: "Pizza With Mushroom",
		price: 110.0,
		likes: 20,
		image01: product_04_image_02,
		image02: product_04_image_01,
		image03: product_04_image_03,
		category: "Pizza",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "10",
		title: "Classic Hamburger",
		price: 24.0,
		likes: 130,
		image01: product_05_image_02,
		image02: product_05_image_01,
		image03: product_05_image_03,
		category: "Burger",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "11",
		title: "Maki Roll ",
		price: 35.0,
		likes: 76,
		image01: product_06_image_01,
		image02: product_06_image_02,
		image03: product_06_image_03,
		category: "Sushi",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "12",
		title: "Philadelphia Roll ",
		price: 35.0,
		likes: 80,
		image01: product_06_image_02,
		image02: product_06_image_01,
		image03: product_06_image_03,
		category: "Sushi",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},

	{
		id: "13",
		title: "Sashimi Roll ",
		price: 35.0,
		likes: 100,
		image01: product_06_image_03,
		image02: product_06_image_02,
		image03: product_06_image_03,
		category: "Sushi",

		desc: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.",
	},
];

export default products;
