import React, { useState } from "react";
import { useSelector } from "react-redux";
import { Col, Container, Row } from "react-bootstrap";
import jwt_decode from "jwt-decode";

import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import "./Checkout.css";

const Checkout = () => {
	const [delivery, setDelivery] = useState(true);

	const [enterName, setEnterName] = useState("");
	const [enterEmail, setEnterEmail] = useState("");
	const [enterNumber, setEnterNumber] = useState("");

	const [enterCity, setEnterCity] = useState("");
	const [enterStreet, setEnterStreet] = useState("");
	const [houseNumber, setHouseNumber] = useState("");
	const [flatNumber, setFlatNumber] = useState("");
	const [floor, setFloor] = useState("");

	const shippingInfo = [];
	const cartTotalAmount = useSelector((state) => state.cart.totalAmount);
	const [shippingCost, setShippingCost] = useState("40");
	const totalAmount = cartTotalAmount + Number(shippingCost);

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);

	const submitHandler = (e) => {
		e.preventDefault();
		const userShippingAddress = {
			name: enterName,
			email: enterEmail,
			phone: enterNumber,

			city: enterCity,
			street: enterStreet,
			house_number: houseNumber,
			flat_number: flatNumber,
			floor: floor,
		};

		shippingInfo.push(userShippingAddress);
		console.log(shippingInfo);
	};

	return (
		<Helmet title="Checkout">
			<CommonAd title="Checkout" />
			<section>
				<Container>
					<Row>
						<Col lg="8" md="6">
							<button
								className="addToCart__btn mb-4"
								onClick={() => {
									setDelivery(false);
									setShippingCost("0");
								}}
							>
								Pickup
							</button>
							<button
								className="addToCart__btn mb-4 ms-4"
								onClick={() => {
									setDelivery(true);
									setShippingCost("40");
								}}
							>
								Delivery
							</button>
							<h4 className="mb-4">Shipping Address</h4>
							<h6 className="mb-4">Basic Info:</h6>
							<form className="checkout__form" onSubmit={submitHandler}>
								<div className="form__group">
									<input
										type="text"
										placeholder="Enter your name"
										required
										onChange={(e) => setEnterName(e.target.value)}
									/>
								</div>

								<div className="form__group">
									<input
										type="email"
										placeholder="Enter your email"
										required
										onChange={(e) => setEnterEmail(e.target.value)}
									/>
								</div>
								<div className="form__group">
									<input
										type="number"
										placeholder="Phone number"
										required
										onChange={(e) => setEnterNumber(e.target.value)}
									/>
								</div>
								{delivery && (
									<>
										<h6 className="mb-4">Address:</h6>
										<div className="form__group">
											<input
												type="text"
												placeholder="City"
												required
												onChange={(e) => setEnterCity(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<input
												type="text"
												placeholder="Street"
												required
												onChange={(e) => setEnterStreet(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<input
												type="number"
												placeholder="House number"
												required
												onChange={(e) => setHouseNumber(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<input
												type="number"
												placeholder="Flat number"
												required
												onChange={(e) => setFlatNumber(e.target.value)}
											/>
										</div>

										<div className="form__group">
											<input
												type="number"
												placeholder="Floor number"
												required
												onChange={(e) => setFloor(e.target.value)}
											/>
										</div>
									</>
								)}
								<h6 className="mb-4">Data & Time:</h6>
								<div className="form__group">
									<input
										type="number"
										placeholder="Data"
										required
										onChange={(e) => setFlatNumber(e.target.value)}
									/>
								</div>

								<div className="form__group">
									<input
										type="number"
										placeholder="Time"
										required
										onChange={(e) => setFloor(e.target.value)}
									/>
								</div>

								<button type="submit" className="addToCart__btn">
									Payment
								</button>
							</form>
						</Col>

						<Col lg="4" md="6">
							<div className="checkout__bill">
								<h6 className="d-flex align-items-center justify-content-between mb-3">
									Subtotal: <span>${cartTotalAmount}</span>
								</h6>
								<h6 className="d-flex align-items-center justify-content-between mb-3">
									Shipping: <span>${shippingCost}</span>
								</h6>
								<div className="checkout__total">
									<h5 className="d-flex align-items-center justify-content-between">
										Total: <span>${totalAmount}</span>
									</h5>
								</div>
							</div>
						</Col>
					</Row>
				</Container>
			</section>
		</Helmet>
	);
};

export default Checkout;
