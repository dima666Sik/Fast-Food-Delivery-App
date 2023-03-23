import React, { useState, useEffect } from "react";
import { Button, Carousel } from "react-bootstrap";
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
		Promise.all([
			import("../../assets/images/slider/first_pizza_slide.png"),
			import("../../assets/images/slider/second_img_for_slider.png"),
			import("../../assets/images/slider/third_img_for_slider.png"),
		]).then(([first, second, third]) => {
			setImages([first.default, second.default, third.default]);
		});
	}, []);

	return (
		<Carousel>
			{images.map((img, index) => (
				<Carousel.Item key={index} style={{ height: "50%" }}>
					<img className="d-block w-100" src={img} alt={`Slide ${index + 1}`} />
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
