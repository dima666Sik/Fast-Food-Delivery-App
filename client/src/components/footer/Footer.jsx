import React from "react";
import {
	Nav,
	Navbar,
	Container,
	Row,
	Col,
	ListGroup,
	ListGroupItem,
} from "react-bootstrap";
import { Link } from "react-router-dom";

import "./Footer.css";
import { useValidationAuthForms } from "../../hooks/useValidationAuthForms";

const Footer = () => {
	const {
		email,
		setEmail,
		emailDirty,
		setEmailDirty,
		emailError,
		formValid,
		emailHandler,
		blurHandler,
	} = useValidationAuthForms();

	const handleRegisterClick = () => {
		setEmailDirty(false);
		setEmail("");
	};

	return (
		<div className="footer__container">
			<Container>
				<Row>
					<Col lg="3" md="4" sm="6">
						<div className="logo__container">
							<Navbar.Brand>
								<Nav.Link as={Link} to="/">
									<h5>Fast Food Dev</h5>
								</Nav.Link>
							</Navbar.Brand>
							<p>
								We offer a variety of pizzas, hamburgers, and sushi for
								delivery. Our fast and reliable service lets you enjoy your
								favorite fast food dishes from home.
							</p>
						</div>
					</Col>

					<Col lg="3" md="4" sm="6">
						<h5>Delivery Time</h5>
						<ListGroup className="delivery__time__list">
							<ListGroupItem className="delivery__time__item border-0 ps-0">
								<span>Sunday - Thursday</span>
								<p>10:00am - 11:00pm</p>
							</ListGroupItem>

							<ListGroupItem className="delivery__time__item border-0 ps-0">
								<span>Friday - Saturday</span>
								<p>Off Day</p>
							</ListGroupItem>
						</ListGroup>
					</Col>

					<Col lg="3" md="4" sm="6">
						<h5>Contact</h5>
						<ListGroup className="delivery__time__list">
							<ListGroupItem className="delivery__time__item border-0 ps-0">
								<span>Location: Ukraine, Chernihiv</span>
							</ListGroupItem>
							<ListGroupItem className="delivery__time__item border-0 ps-0">
								<span>Phone: +380988657488</span>
							</ListGroupItem>

							<ListGroupItem className="delivery__time__item border-0 ps-0">
								<span>fastfooddev@gmail.com</span>
							</ListGroupItem>
						</ListGroup>
					</Col>

					<Col lg="3" md="4" sm="6">
						<h5>Newsletters</h5>
						<p>Subscribe our newsletter</p>
						{emailDirty && emailError && (
							<p className="text-danger">{emailError}</p>
						)}
						<div className="newsletter">
							<input
								placeholder="Enter your e-mail"
								onChange={(e) => emailHandler(e)}
								value={email}
								onBlur={(e) => blurHandler(e)}
								name="email"
								type="email"
							/>
							<span disabled={!formValid}>
								<i className="bi bi-send-fill"></i>
							</span>
						</div>
					</Col>
				</Row>

				<Row className="mt-5">
					<Col lg="6" md="6">
						<p className="copyright__text">
							Copyright - 2023, website made by Dmytro Kohol. All Rights
							Reserved.
						</p>
					</Col>
					<Col lg="6" md="6">
						<div className="social__links d-flex align-item-center gap-4 justify-content-end">
							<p className="m-0">Follow:</p>
							<span>
								<Link to="https://uk-ua.facebook.com/" target="_blank">
									<i className="bi bi-facebook"></i>
								</Link>
							</span>

							<span>
								<Link to="https://www.instagram.com/" target="_blank">
									<i className="bi bi-instagram"></i>
								</Link>
							</span>

							<span>
								<Link to="https://www.youtube.com/" target="_blank">
									<i className="bi bi-youtube"></i>
								</Link>
							</span>
						</div>
					</Col>
				</Row>
			</Container>
		</div>
	);
};

export default Footer;
