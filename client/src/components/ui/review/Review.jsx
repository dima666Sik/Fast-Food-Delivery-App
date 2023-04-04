import React from "react";
import { Container, Row, Col } from "react-bootstrap";

import reviewImg from "../../../assets/images/review/reviewImg.png";
import ReviewSlider from "../review-slider/ReviewSlider";

const Review = () => {
	return (
		<Container>
			<Row>
				<Col lg="6" md="6">
					<div className="review ">
						<h5 className="review__subtitle mb-4">Testimonial</h5>
						<h2 className="review__title mb-4">
							What our <span>customers</span> are saying
						</h2>
						<p className="review__desc">
							Lorem ipsum dolor sit amet consectetur, adipisicing elit.
							Distinctio quasi qui minus quos sit perspiciatis inventore quis
							provident placeat fugiat!
						</p>

						<ReviewSlider />
					</div>
				</Col>

				<Col lg="6" md="6">
					<img src={reviewImg} alt="testimonial-img" className="w-100" />
				</Col>
			</Row>
		</Container>
	);
};

export default Review;
