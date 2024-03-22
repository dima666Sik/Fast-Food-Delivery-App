import React from "react";
import { Container, Row, Col, ListGroup, ListGroupItem } from "react-bootstrap";

import whyImg from "../../../../assets/images/why-we/whyWeImg.png";
import "./WhyWe.css";

const WhyWe = () => {
	return (
		<Container>
			<Row>
				<Col lg="6" md="6">
					<img src={whyImg} alt="why-fast-food" className="w-100" />
				</Col>

				<Col lg="6" md="6">
					<div className="why__fast-food">
						<h2 className="fast__food__title mb-4">
							Why <span>Fast Food Dev?</span>
						</h2>
						<p className="fast__food__desc">
							Fast Food Dev is a convenient platform that offers a wide range of
							fast food options to satisfy cravings quickly and easily. With its
							user-friendly website, customers can easily navigate through
							menus, do orders. The platform's simplicity and efficiency make it
							a convenient choice for those looking for a hassle-free fast food
							experience.
						</p>

						<ListGroup className="mt-4">
							<ListGroupItem className="border-0 ps-0">
								<p className="choose__us__title d-flex align-items-center gap-2 ">
									<i className="bi bi-check2-circle"></i> Fresh and tasty foods
								</p>
								<p className="choose__us__desc">
									Fresh and tasty foods are prepared in the kitchen.
								</p>
							</ListGroupItem>

							<ListGroupItem className="border-0 ps-0">
								<p className="choose__us__title d-flex align-items-center gap-2 ">
									<i className="bi bi-check2-circle"></i> Quality support
								</p>
								<p className="choose__us__desc">
									For quality support, you can send your suggestions or
									questions on email.
								</p>
							</ListGroupItem>

							<ListGroupItem className="border-0 ps-0">
								<p className="choose__us__title d-flex align-items-center gap-2 ">
									<i className="bi bi-check2-circle"></i>Easy order
								</p>
								<p className="choose__us__desc">
									Enjoy easy food ordering on our website.
								</p>
							</ListGroupItem>
						</ListGroup>
					</div>
				</Col>
			</Row>
		</Container>
	);
};

export default WhyWe;
