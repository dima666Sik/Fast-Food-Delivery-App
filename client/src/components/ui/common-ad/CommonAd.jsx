import React from "react";
import { Container } from "react-bootstrap";
import "./CommonAd.css";

const CommonAd = (props) => {
	return (
		<section className="common__section__ad">
			<Container>
				<h2 className="text-white">{props.title}</h2>
			</Container>
		</section>
	);
};

export default CommonAd;
