import React from "react";
import { Container, Row, Col, ListGroup, ListGroupItem } from "react-bootstrap";
import whyImg from "../../../assets/images/why-we/whyWeImg.png";
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
						<h2 className="fast__food-title mb-4">
							Why <span>Fast Food Dev?</span>
						</h2>
						<p className="fast__food-desc">
							Lorem ipsum dolor sit amet consectetur adipisicing elit. Dolorum,
							minus. Tempora reprehenderit a corporis velit, laboriosam vitae
							ullam, repellat illo sequi odio esse iste fugiat dolor, optio
							incidunt eligendi deleniti!
						</p>

						<ListGroup className="mt-4">
							<ListGroupItem className="border-0 ps-0">
								<p className="choose__us-title d-flex align-items-center gap-2 ">
									<i className="bi bi-check2-circle"></i> Fresh and tasty foods
								</p>
								<p className="choose__us-desc">
									Lorem ipsum, dolor sit amet consectetur adipisicing elit.
									Quia, voluptatibus.
								</p>
							</ListGroupItem>

							<ListGroupItem className="border-0 ps-0">
								<p className="choose__us-title d-flex align-items-center gap-2 ">
									<i className="bi bi-check2-circle"></i> Quality support
								</p>
								<p className="choose__us-desc">
									Lorem ipsum dolor sit amet consectetur adipisicing elit. Qui,
									earum.
								</p>
							</ListGroupItem>

							<ListGroupItem className="border-0 ps-0">
								<p className="choose__us-title d-flex align-items-center gap-2 ">
									<i className="bi bi-check2-circle"></i>Order from any location{" "}
								</p>
								<p className="choose__us-desc">
									Lorem ipsum dolor sit amet consectetur adipisicing elit. Qui,
									earum.
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
