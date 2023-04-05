import React from "react";
import { Col, Container, Row } from "react-bootstrap";
import "./Contact.css";
import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";

const Contact = () => {
	return (
		<Helmet title="Contact">
			<CommonAd title="Contact" />
			<Container>
				<Row className="sec_sp pb-4">
					<Col lg="5" className="mb-5">
						<h3 className="color_sec py-4">Get in touch</h3>
						<address>
							<p>
								<strong>Email:</strong> fastfooddev@gmail.com
							</p>
							<p>
								<strong>Phone:</strong> +380888999777
							</p>
						</address>
						<p>Description</p>
					</Col>
					<Col lg="6" className="d-flex align-items-center">
						<form className="checkout__form">
							<div className="d-flex pt-4">
								<div className="form__group text__name">
									<input type="text" placeholder="Enter your name" required />
								</div>
								<div className="form__group">
									<input type="email" placeholder="Enter your email" required />
								</div>
							</div>
							<div className="form__group">
								<textarea
									className="rounded-0 w-100"
									rows={5}
									type="text"
									placeholder="Write your review"
									required
								/>
							</div>

							<button type="submit" className="addToCart__btn">
								Send
							</button>
						</form>
					</Col>
				</Row>
			</Container>
		</Helmet>
	);
};

export default Contact;
