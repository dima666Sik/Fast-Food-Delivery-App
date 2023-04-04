import React from "react";
import { Form, Modal, Button } from "react-bootstrap";

import { useFormValidation } from "../hooks/validationForms";
import AlertText from "../components/alerts/alert-text/AlertText";

const Register = (props) => {
	const {
		email,
		setEmail,
		password,
		setPassword,
		emailDirty,
		setEmailDirty,
		passwordDirty,
		setPasswordDirty,
		emailError,
		passwordError,
		formValid,
		setFormValid,
		emailHandler,
		passwordHandler,
		confirmPassword,
		confirmPasswordDirty,
		confirmPasswordError,
		confirmPasswordHandler,
		setConfirmPasswordDirty,
		setConfirmPassword,
	} = useFormValidation();

	const handleLoginClick = () => {
		setEmailDirty(false);
		setPasswordDirty(false);
		setConfirmPasswordDirty(false);
		setEmail("");
		setPassword("");
		setConfirmPassword("");
		props.onHide();
		props.onLoginClick();
		setFormValid(false);
	};

	return (
		<>
			<Modal
				aria-labelledby="contained-modal-title-vcenter"
				centered
				show={props.show}
				onHide={props.onHide}
			>
				<Modal.Header closeButton>
					<Modal.Title>Sign Up</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
						<Form.Group controlId="fromBasicFirstName">
							<div className="d-flex justify-content-between">
								<Form.Label>First Name</Form.Label>
							</div>
							<Form.Control type="text" placeholder="Enter first name" />
						</Form.Group>

						<Form.Group controlId="fromBasicLastName">
							<div className="d-flex justify-content-between">
								<Form.Label>Last Name</Form.Label>
							</div>
							<Form.Control type="text" placeholder="Enter last name" />
						</Form.Group>

						<Form.Group controlId="fromBasicEmail">
							<div className="d-flex justify-content-between">
								<Form.Label>E-mail</Form.Label>
								<AlertText
									paramDirty={emailDirty}
									paramError={emailError}
									paramSuccess="E-mail is good!"
								/>
							</div>
							<Form.Control
								onChange={(e) => emailHandler(e)}
								value={email}
								name="email"
								type="email"
								placeholder="Enter e-mail"
							/>
						</Form.Group>

						<Form.Group controlId="fromBasicPassword">
							<div className="d-flex justify-content-between">
								<Form.Label>Password</Form.Label>
								<AlertText
									paramDirty={passwordDirty}
									paramError={passwordError}
									paramSuccess="Password is good!"
								/>
							</div>
							<Form.Control
								onChange={(e) => passwordHandler(e)}
								value={password}
								name="password"
								type="password"
								placeholder="Enter password"
							/>
						</Form.Group>

						<Form.Group controlId="fromBasicConfirmPassword">
							<div className="d-flex justify-content-between">
								<Form.Label>Confirm the password</Form.Label>
								<AlertText
									paramDirty={confirmPasswordDirty}
									paramError={confirmPasswordError}
									paramSuccess="Confirm password is good!"
								/>
							</div>
							<Form.Control
								onChange={(e) => confirmPasswordHandler(e)}
								value={confirmPassword}
								name="confirmPassword"
								type="password"
								placeholder="Enter confirm password"
							/>
						</Form.Group>

						<Modal.Footer>
							<Button variant="secondary" onClick={handleLoginClick}>
								Sign In
							</Button>
							<Button
								disabled={!formValid}
								className="text-light"
								variant="primary"
								style={{
									backgroundColor: "orangered",
									borderColor: "orangered",
								}}
							>
								Sign Up
							</Button>
						</Modal.Footer>
						<Form.Text>
							If you have an account, click on 'Sign In' to enter one.
						</Form.Text>
					</Form>
				</Modal.Body>
			</Modal>
		</>
	);
};

export default Register;
