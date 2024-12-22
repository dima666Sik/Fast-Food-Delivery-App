import React, { useState, useEffect } from "react";
import { Button, Carousel, Spinner } from "react-bootstrap";
import axios from "axios";

import "./Slider.css";
import { Link } from "react-router-dom";

const SliderCaption = ({ title, description }) => {
	return (
		<Carousel.Caption>
			<div className="block__title d-flex justify-content-center">
				<h3 className="slider__title">{title}</h3>
				<Button variant="primary" className="order__button">
					<Link to="/checkout">
						Order Now <i className="bi bi-chevron-right"></i>
					</Link>
				</Button>
			</div>
			<p className="slider-description">{description}</p>
		</Carousel.Caption>
	);
};

const Slider = () => {
	const [images, setImages] = useState([]);
	const [isLoadingSlider, setIsLoadingSlider] = useState(false);
	useEffect(() => {
		setIsLoadingSlider(true);
		axios
			.get(
				`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/slider/images`
			)
			.then((response) => {
				const imageUrls = response.data.map((image) => image.urlImg);
				console.log(imageUrls);
				setImages(imageUrls);
				setIsLoadingSlider(false);
			})
			.catch((error) => {
				console.log(error);
				setIsLoadingSlider(false);
			});
	}, []);

	return isLoadingSlider ? (
		<div className="text-center">
			<Spinner />
		</div>
	) : images.length === 0 ? (
		<div className="review pt-5 mb-5 pb-5">
			<p className="user__name text-center my-0">Slider is absent</p>
		</div>
	) : (
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
							index === 0 ? "burger" : index === 1 ? "pizza" : "sushi"
						}?`}
						description="Order delivery from us, incredible taste and quality are guaranteed."
					/>
				</Carousel.Item>
			))}
		</Carousel>
	);
};

export default Slider;
