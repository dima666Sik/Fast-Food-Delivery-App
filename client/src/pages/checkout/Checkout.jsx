import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Col, Container, Row } from "react-bootstrap";
import PhoneInput from "react-phone-input-2";
// import { Link } from "react-router-dom";

import "react-phone-input-2/lib/style.css";
import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import ModalAlert from "../../components/alerts/ModalAlert";
import AlertText from "../../components/alerts/alert-text/AlertText";
import { useValidationAuthForms } from "../../hooks/useValidationAuthForms";
import "./Checkout.css";

const Checkout = () => {
	const [delivery, setDelivery] = useState(true);

	const [enterNumber, setEnterNumber] = useState("");

	const [enterCity, setEnterCity] = useState("");
	const [enterStreet, setEnterStreet] = useState("");
	const [houseNumber, setHouseNumber] = useState("");
	const [flatNumber, setFlatNumber] = useState("");
	const [floor, setFloor] = useState("");

	const [date, setDate] = useState("");
	const [time, setTime] = useState("");

	const shippingInfo = [];
	const cartTotalAmount = useSelector((state) => state.cart.totalAmount);
	const [shippingCost, setShippingCost] = useState("40");
	const totalAmount = cartTotalAmount + Number(shippingCost);
	const [showModal, setShowModal] = useState(false);

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const userName = useSelector((state) => state.user.firstName);
	const userEmail = useSelector((state) => state.user.email);

	const cartItems = useSelector((state) => state.cart.cartItems);

	const {
		email,
		setEmail,
		emailDirty,
		emailError,
		emailHandler,
		firstName,
		setFirstName,
	} = useValidationAuthForms();

	useEffect(() => {
		if (isAuthenticated && userEmail !== null) setEmail(userEmail);
		if (isAuthenticated && userName !== null) setFirstName(userName);
	}, [userEmail]);

	const submitHandler = (e) => {
		e.preventDefault();
		console.log(cartTotalAmount, enterNumber);
		console.log(email, firstName);
		if (cartTotalAmount === 0) {
			setShowModal(true);
		} else {
			const userShippingAddress = {
				name: firstName,
				email: email,
				phone: enterNumber,

				city: enterCity,
				street: enterStreet,
				house_number: houseNumber,
				flat_number: flatNumber,
				floor: floor,

				date: date,
				time: time,
			};

			shippingInfo.push(userShippingAddress);
			console.log(shippingInfo);

			const newList = cartItems.map(({ id, totalPrice, quantity }) => ({
				id,
				totalPrice,
				quantity,
			}));
			console.log(newList);
		}
	};

	const getCurrentDate = () => {
		const today = new Date();
		const year = today.getFullYear();
		let month = today.getMonth() + 1;
		let day = today.getDate();

		if (month < 10) {
			month = "0" + month;
		}

		if (day < 10) {
			day = "0" + day;
		}

		return `${year}-${month}-${day}`;
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
									<label htmlFor="firstName">Name:</label>
									<input
										type="text"
										id="firstName"
										placeholder="Enter your name"
										value={firstName}
										required
										onChange={(e) => setFirstName(e.target.value)}
										disabled={isAuthenticated}
									/>
								</div>

								<div className="form__group individual_email_phone">
									<div className="f_g_container_email_phone">
										<label htmlFor="email">Email:</label>
										<input
											id="email"
											placeholder="Enter your email"
											onChange={(e) => emailHandler(e)}
											value={email}
											name="email"
											type="email"
											required
											disabled={isAuthenticated}
										/>
										{!isAuthenticated && (
											<AlertText
												paramDirty={emailDirty}
												paramError={emailError}
												paramSuccess="E-mail is good!"
											/>
										)}
									</div>
									<div>
										<label htmlFor="phoneNumber">Phone Number:</label>
										<PhoneInput
											id="phoneNumber"
											country={"ua"}
											value={enterNumber}
											onChange={(e) => setEnterNumber(e)}
										/>
									</div>
								</div>

								{delivery && (
									<>
										<h6 className="mb-4">Address:</h6>
										<div className="form__group">
											<label htmlFor="city">City:</label>
											<input
												id="city"
												type="text"
												placeholder="Enter your City"
												required
												onChange={(e) => setEnterCity(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<label htmlFor="street">Street:</label>
											<input
												id="street"
												type="text"
												placeholder="Enter your Street"
												required
												onChange={(e) => setEnterStreet(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<label htmlFor="houseNumber">House Number:</label>
											<input
												id="houseNumber"
												type="number"
												placeholder="Enter your House number"
												required
												onChange={(e) => setHouseNumber(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<label htmlFor="flatNumber">Flat Number:</label>
											<input
												id="flatNumber"
												type="number"
												placeholder="Enter your Flat number"
												required
												onChange={(e) => setFlatNumber(e.target.value)}
											/>
										</div>
										<div className="form__group">
											<label htmlFor="floorNumber">Floor Number:</label>
											<input
												id="floorNumber"
												type="number"
												placeholder="Enter your Floor number"
												required
												onChange={(e) => setFloor(e.target.value)}
											/>
										</div>
									</>
								)}

								<h6 className="mb-4">Date & Time:</h6>
								<div className="form__group">
									<label htmlFor="date">Date:</label>
									<input
										id="date"
										type="date"
										placeholder="Enter your Date"
										required
										min={getCurrentDate()}
										onChange={(e) => setDate(e.target.value)}
									/>
								</div>

								<div className="form__group">
									<label htmlFor="time">Time:</label>
									<fieldset>
										<input
											list="time-list"
											id="time"
											type="time"
											placeholder="Enter your Time"
											required
											onChange={(e) => setTime(e.target.value)}
										/>
										<datalist id="time-list">
											<option value="09:00" />
											<option value="10:00" />
											<option value="11:00" />
											<option value="12:00" />
											<option value="14:00" />
											<option value="16:00" />
											<option value="17:00" />
											<option value="18:00" />
											<option value="20:00" />
										</datalist>
									</fieldset>
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
			{showModal && (
				<ModalAlert
					paramTitle={"General Message"}
					paramBody={
						"You have not selected any products, please select products."
					}
					onShow={showModal}
					onHide={() => setShowModal(false)}
				/>
			)}
		</Helmet>
	);
};

export default Checkout;
