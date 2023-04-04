import React from "react";
import { Form, Modal, Button } from "react-bootstrap";

import { useFormValidation } from "../hooks/validationForms";
import AlertText from "../components/alerts/alert-text/AlertText";

const Login = (props) => {
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
	} = useFormValidation();

	const handleRegisterClick = () => {
		setEmailDirty(false);
		setPasswordDirty(false);
		setEmail("");
		setPassword("");
		props.onHide();
		props.onRegisterClick();
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
					<Modal.Title>Log In</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
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
							<Form.Text className="text-muted">
								We will never share your e-mail with anyone else.
							</Form.Text>
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
							<Form.Text className="text-muted">
								We will never share your password with anyone else.
							</Form.Text>
						</Form.Group>

						<Modal.Footer>
							<Button variant="secondary" onClick={handleRegisterClick}>
								Sign Up
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
								Sign In
							</Button>
						</Modal.Footer>
						<Form.Text>
							If you do not have an account, click on 'Sign Up' to create one.
						</Form.Text>
					</Form>
				</Modal.Body>
			</Modal>
		</>
	);
};

export default Login;
