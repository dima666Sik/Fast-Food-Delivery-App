import React, { useState, useEffect } from "react";
import { Button, Carousel } from "react-bootstrap";
import axios from "axios";

import "./Slider.css";

const SliderCaption = ({ title, description }) => {
	return (
		<Carousel.Caption>
			<div className="block__title d-flex justify-content-center">
				<h3 className="slider__title">{title}</h3>
				<Button variant="primary" className="order__button">
					Order Now <i className="bi bi-chevron-right"></i>
				</Button>
			</div>
			<p className="slider-description">{description}</p>
		</Carousel.Caption>
	);
};

const Slider = () => {
	const [images, setImages] = useState([]);

	useEffect(() => {
		axios
			.get(`${process.env.REACT_APP_SERVER_API_URL}api/v1/slider/images`)
			.then((response) => {
				const imageUrls = response.data.map((image) => image.urlImg);
				setImages(imageUrls);
			})
			.catch((error) => console.log(error));
	}, []);

	return (
		<Carousel>
			{images.map((img, index) => (
				<Carousel.Item key={index} style={{ height: "50%" }}>
					<img
						className="d-block w-100"
						src={`${process.env.REACT_APP_SERVER_API_URL}public/images/slider/${img}`}
						alt={`Slide ${index + 1}`}
					/>
					<SliderCaption
						title={`Do you want ${
							index === 0 ? "pizza" : index === 1 ? "burger" : "sushi"
						}?`}
						description="Order delivery from us, incredible taste and quality are guaranteed."
					/>
				</Carousel.Item>
			))}
		</Carousel>
	);
};

export default Slider;
