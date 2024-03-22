import React from "react";
import { Container, Row, Col } from "react-bootstrap";

import featureImg01 from "../../../../assets/images/aboutus/about__us__img1.png";
import featureImg02 from "../../../../assets/images/aboutus/about__us__img2.png";
import featureImg03 from "../../../../assets/images/aboutus/about__us__img3.png";

const featureData = [
	{
		title: "Quick Delivery",
		imgUrl: featureImg01,
		desc: "Fast delivery is a convenient option for customers who prioritize timely service.",
	},

	{
		title: "Super Dine In",
		imgUrl: featureImg02,
		desc: "We offers a wide selection of delicious food options.",
	},
	{
		title: "Easy Pick Up",
		imgUrl: featureImg03,
		desc: "It is easy to pick up the purchase.",
	},
];

const AboutUs = () => {
	return (
		<Container>
			<Row>
				<Col lg="12" className="text-center">
					<h5 className="feature__subtitle mb-4">What we serve</h5>
					<h2 className="feature__title">Just sit back at home</h2>
					<h2 className="feature__title">
						we will <span>take care</span>
					</h2>
					<p className="mb-1 mt-4 feature__text">
						Fast delivery is our top priority, ensuring that your order reaches
						you quickly and efficiently.
					</p>
					<p className="feature__text">
						We strive to provide speedy and reliable delivery service to ensure
						your satisfaction.
					</p>
				</Col>

				{featureData.map((item, index) => (
					<Col lg="4" md="6" sm="6" key={index} className="mt-5">
						<div className="feature__item text-center px-5 py-3">
							<img src={item.imgUrl} alt="feature-img" className="w-25 mb-3" />
							<h5 className=" fw-bold mb-3">{item.title}</h5>
							<p>{item.desc}</p>
						</div>
					</Col>
				))}
			</Row>
		</Container>
	);
};

export default AboutUs;
