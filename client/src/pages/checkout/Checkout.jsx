import React, { useEffect, useState } from "react";
import { Col, Container, Row, Spinner } from "react-bootstrap";
import PhoneInput from "react-phone-input-2";
import { useDispatch, useSelector } from "react-redux";

import "react-phone-input-2/lib/style.css";
import {
	addOrderPurchaseGuest,
	addOrderPurchaseUser,
} from "../../actions/post/addOrderPurchase";
import { refresh } from "../../actions/post/refresh";
import AlertText from "../../components/alerts/AlertText";
import ModalAlert from "../../components/alerts/ModalAlert";
import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import { useValidationAuthForms } from "../../hooks/useValidationAuthForms";
import { useValidFormsBtn } from "../../hooks/useValidFormsBtn";
import { axiosLogout, setUser } from "../../redux/store/user/userSlice";
import { roundToTwoDecimals } from "../../utils/utils";
import "./Checkout.css";
import { Elements } from "@stripe/react-stripe-js";
import CheckoutStripeForm from "./CheckoutStripeForm";
import { loadStripe } from "@stripe/stripe-js";

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

	const cartTotalAmount = useSelector((state) => state.cart.totalAmount);
	const [shippingCost, setShippingCost] = useState("40");
	const totalAmount = roundToTwoDecimals(
		cartTotalAmount + Number(shippingCost)
	);
	const [showModal, setShowModal] = useState(false);

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const userName = useSelector((state) => state.user.firstName);
	const userEmail = useSelector((state) => state.user.email);
	const accessToken = useSelector((state) => state.user.accessToken);

	const cartItems = useSelector((state) => state.cart.cartItems);
	const dispatch = useDispatch();
	const [showTextModal, setShowTextModal] = useState("");

	const { formValid, setFormValid } = useValidFormsBtn();
	const [isLoadingSender, setIsLoadingSender] = useState(false);
	const [isCashPayment, setIsCashPayment] = useState(true);

	const {
		email,
		setEmail,
		emailDirty,
		emailError,
		emailHandler,
		firstName,
		setFirstName,
		setEmailError,
		setEmailDirty,
	} = useValidationAuthForms();

	useEffect(() => {
		console.log(
			emailError,
			!enterNumber,
			!enterCity,
			!enterStreet,
			!houseNumber,
			!flatNumber,
			!floor,
			!date,
			!time
		);
		if (isAuthenticated) {
			if (delivery) {
				if (
					!enterNumber ||
					!enterCity ||
					!enterStreet ||
					!houseNumber ||
					!flatNumber ||
					!floor ||
					!date ||
					!time
				)
					setFormValid(false);
				else setFormValid(true);
			} else {
				if (!enterNumber || !date || !time) setFormValid(false);
				else setFormValid(true);
			}
		} else {
			if (delivery) {
				if (
					!enterNumber ||
					!enterCity ||
					!enterStreet ||
					!houseNumber ||
					!flatNumber ||
					!floor ||
					!date ||
					!time
				)
					setFormValid(false);
				else setFormValid(true);
			} else {
				if (!enterNumber || !date || !time) setFormValid(false);
				else setFormValid(true);
			}
		}
	}, [
		emailError,
		firstName,
		enterNumber,
		enterCity,
		enterStreet,
		houseNumber,
		flatNumber,
		floor,
		date,
		time,
		delivery,
	]);

	useEffect(() => {
		if (isAuthenticated && userEmail !== null) setEmail(userEmail);
		else setEmail("");
		if (isAuthenticated && userName !== null) setFirstName(userName);
		else setFirstName("");
		console.log(isAuthenticated, userEmail, userName);
	}, [isAuthenticated, userEmail, userName]);

	const handleDoOrder = async (e) => {
		e.preventDefault();
		console.log(cartTotalAmount, enterNumber);
		console.log(email, firstName);
		if (cartTotalAmount === 0) {
			setShowTextModal(
				"You have not selected any products, please select products."
			);
			setShowModal(true);
		} else {
			const newList = cartItems.map(({ id, totalPrice, quantity }) => ({
				id,
				totalPrice,
				quantity,
			}));

			const userShippingAddress = {
				name: firstName,
				contact_email: email,
				phone: enterNumber,

				city: enterCity,
				street: enterStreet,
				house_number: houseNumber,
				flat_number: flatNumber,
				floor_number: floor,

				order_date_arrived: date,
				order_time_arrived: time,

				total_amount: totalAmount,
				delivery: delivery,

				cash_payment: isCashPayment,

				purchaseItems: newList,
			};

			console.log(userShippingAddress);
			console.log(newList);

			if (isAuthenticated) {
				try {
					setIsLoadingSender(true);
					const response = await addOrderPurchaseUser(
						accessToken,
						userShippingAddress
					);
					if (response.status === 200) {
						console.log("Order put in db:", response.data);
						setShowTextModal("Order is successful!");
						setShowModal(true);
						if (!isAuthenticated) {
							setEmail("");
							setEmailDirty(false);
							setEmailError("");
							setFirstName("");
						}
						setEnterNumber("");
						setEnterCity("");
						setEnterStreet("");
						setHouseNumber("");
						setFlatNumber("");
						setFloor("");
						setDate("");
						setTime("");
						setIsLoadingSender(false);
					}
				} catch (error) {
					///////////////////////////
					console.log(error, accessToken);
					if (
						error.response.data.message === "Access token has expired" ||
						error.response.data.message === "Access token has revoked"
					) {
						try {
							const response = await refresh(accessToken);

							if (response.status === 200) {
								if (response.data.access_token) {
									// Dispatch the setUser action
									dispatch(
										setUser({
											accessToken: response.data.access_token,
										})
									);
									setShowTextModal(
										"Tokens was Updated, please continue use site. Please repeat sent your order."
									);
									setShowModal(true);
								}
							}
						} catch (error) {
							console.log(error);
							throw error; // пробрасываем ошибку выше для обработки в setLikes
						}
					}

					if (
						error.response.data.message ===
							"Access & Refresh tokens have expired" ||
						error.response.data.message === "Access token not found" ||
						error.response.data.message === "Refresh token not found" ||
						error.response.data.message === "Invalid token"
					) {
						setShowTextModal(
							"You don't have rights to review. Please Authorization in this Application..."
						);
						setShowModal(true);
						dispatch(
							axiosLogout({
								accessToken,
							})
						);
					}
				}
			} else {
				try {
					setIsLoadingSender(true);
					const response = await addOrderPurchaseGuest(userShippingAddress);
					if (response.status === 200) {
						console.log("Order put in db:", response.data);
						setShowTextModal("Order is successful!");
						setShowModal(true);
						setEnterNumber("");
						setEnterCity("");
						setEnterStreet("");
						setHouseNumber("");
						setFlatNumber("");
						setFloor("");
						setDate("");
						setTime("");
						setIsLoadingSender(false);
					}
				} catch (error) {
					console.log(error);
					console.error(error.response);
					console.error(error.response.data.message);
				}
			}
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

	const [stripePromise, setStripePromise] = useState(null);
	const [clientSecret, setClientSecret] = useState("");

	useEffect(() => {
		fetch(`${process.env.REACT_APP_SERVER_API_URL}api/v2/payment/config`)
			.then(async (r) => {
				console.log(r);
				const { publishableKey } = await r.json();
				console.log("publishableKey", publishableKey);
				setStripePromise(loadStripe(publishableKey));
			})
			.catch((err) => {
				console.error("Error creating payment intent:", err);
			});
	}, []);

	useEffect(() => {
		console.log(totalAmount);
		fetch(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/payment/create-payment-intent`,
			{
				method: "POST",
				headers: {
					"Content-Type": "application/json", // Указывает, что тело запроса в формате JSON
				},
				body: JSON.stringify({ amount: totalAmount }), // Передаём примерную сумму в центах
			}
		)
			.then(async (result) => {
				console.log(result);
				const { client_secret } = await result.json();
				console.log("clientSecret", client_secret);
				setClientSecret(client_secret);
			})
			.catch((err) => {
				console.error("Error creating payment intent:", err);
			});
	}, [totalAmount]);

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
							<div className="payment-method-buttons">
								<button
									className={`addToCart__btn mb-4 ${isCashPayment && "active"}`}
									onClick={() => setIsCashPayment(true)}
								>
									Cash Payment
								</button>
								<button
									className={`addToCart__btn mb-4 ms-4 ${
										!isCashPayment && "active"
									}`}
									onClick={() => setIsCashPayment(false)}
								>
									Card Payment
								</button>
							</div>
							<h4 className="mb-4">Shipping Address</h4>
							<h6 className="mb-4">Basic Info:</h6>
							<form className="checkout__form">
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
												value={enterCity}
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
												value={enterStreet}
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
												value={houseNumber}
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
												value={flatNumber}
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
												value={floor}
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
										value={date}
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
											value={time}
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

								{isLoadingSender ? (
									<div className="text-center">
										<Spinner />
									</div>
								) : isCashPayment ? (
									<button
										disabled={!formValid}
										onClick={handleDoOrder}
										className="addToCart__btn"
									>
										Payment
									</button>
								) : (
									<></>
								)}
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
							<div className="checkout__bill mt-5">
								<h6 className="d-flex justify-content-center">
									{isCashPayment ? (
										"Cash Payment on the spot"
									) : (
										<>
											{clientSecret && stripePromise && (
												<Elements
													stripe={stripePromise}
													options={{ clientSecret }}
												>
													<CheckoutStripeForm handleDoOrder={handleDoOrder} />
												</Elements>
											)}
										</>
									)}
								</h6>
							</div>
						</Col>
					</Row>
				</Container>
			</section>
			{showModal && (
				<ModalAlert
					paramTitle={"General Message"}
					paramBody={showTextModal}
					onShow={showModal}
					onHide={() => setShowModal(false)}
				/>
			)}
		</Helmet>
	);
};

export default Checkout;
