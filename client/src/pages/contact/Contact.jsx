import React, { useEffect, useState } from "react";
import { Col, Container, Row, Spinner } from "react-bootstrap";
import axios from "axios";

import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import "./Contact.css";
import { useValidFormsBtn } from "../../hooks/useValidFormsBtn";
import { useValidationAuthForms } from "../../hooks/useValidationAuthForms";
import AlertText from "../../components/alerts/alert-text/AlertText";
import { useReview } from "../../hooks/useReview";
import ModalAlert from "../../components/alerts/ModalAlert";

import { useSelector } from "react-redux";

const Contact = () => {
	const { formValid, setFormValid } = useValidFormsBtn();
	const [isLoadingSender, setIsLoadingSender] = useState(false);
	const [showModal, setShowModal] = useState(false);
	const [showTextModal, setShowTextModal] = useState("");

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const userName = useSelector((state) => state.user.firstName);
	const userEmail = useSelector((state) => state.user.email);

	const {
		email,
		setEmail,
		emailDirty,
		setEmailDirty,
		setEmailError,
		emailError,
		emailHandler,
		firstName,
		setFirstName,
	} = useValidationAuthForms();

	const {
		review,
		setReview,
		reviewDirty,
		reviewError,
		reviewHandler,
		setReviewError,
		setReviewDirty,
	} = useReview();

	useEffect(() => {
		window.scrollTo(0, 0);
	}, []);

	const handleReviewClick = async (e) => {
		e.preventDefault();

		const userData = {
			username: firstName,
			from: email,
			message: review,
		};

		try {
			setIsLoadingSender(true);

			const response = await axios.post(
				`${process.env.REACT_APP_SERVER_API_URL}api/v1/email/contact-review`,
				userData
			);

			if (response.status === 200) {
				console.log(userData);
				console.log("Review: ", response.data);
				if (!isAuthenticated) {
					setEmail("");
					setEmailDirty(false);
					setEmailError("");
					setFirstName("");
				}
				setReview("");
				setReviewDirty(false);
				setReviewError("");
				setIsLoadingSender(false);
				setShowTextModal("Your letter was successfully sent to the mail");
				setShowModal(true);
			} else {
				setShowTextModal("Email was not sent...");
				setShowModal(true);
				throw new Error("Registration failed");
			}
		} catch (error) {
			console.error(error);
		}
	};

	useEffect(() => {
		if (isAuthenticated && userEmail !== null) setEmail(userEmail);
		if (isAuthenticated && userName !== null) setFirstName(userName);
	}, [userEmail]);

	useEffect(() => {
		console.log(emailError, !firstName, !review);
		if (isAuthenticated) {
			if (!review) setFormValid(false);
			else setFormValid(true);
		} else {
			if (emailError || !firstName || !review) setFormValid(false);
			else setFormValid(true);
		}
	}, [emailError, firstName, review]);

	return (
		<Helmet title="Contact">
			<CommonAd title="Contact" />
			<Container>
				<Row className="sec_sp pb-4">
					<Col lg="5" className="mb-5">
						<h3 className="color_sec py-4">Get in touch</h3>
						<address>
							<p>
								<strong>Email:</strong> leniviy.demon.1@gmail.com
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
									<input
										placeholder="Enter your name"
										onChange={(e) => {
											setFirstName(e.target.value);
										}}
										value={firstName}
										name="firstName"
										type="text"
										required
										disabled={isAuthenticated}
									/>
								</div>
								<div className="form__group">
									<input
										placeholder="Enter your email"
										onChange={(e) => emailHandler(e)}
										value={email}
										name="email"
										type="email"
										required
										disabled={isAuthenticated}
									/>
									<AlertText
										paramDirty={emailDirty}
										paramError={emailError}
										paramSuccess="E-mail is good!"
									/>
								</div>
							</div>
							<div className="form__group">
								<textarea
									onChange={(e) => reviewHandler(e)}
									value={review}
									className="rounded-0 w-100"
									rows={5}
									type="text"
									placeholder="Write your review"
									required
								/>
								<AlertText
									paramDirty={reviewDirty}
									paramError={reviewError}
									paramSuccess="Review is good!"
								/>
							</div>

							{isLoadingSender ? (
								<div className="text-center">
									<Spinner />
								</div>
							) : (
								<button
									className="addToCart__btn"
									disabled={!formValid}
									onClick={handleReviewClick}
								>
									Send
								</button>
							)}
						</form>
					</Col>
				</Row>
			</Container>
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

export default Contact;
