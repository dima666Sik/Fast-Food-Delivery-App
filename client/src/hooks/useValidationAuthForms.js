import { useState } from "react";

export const useValidationAuthForms = () => {
	const [firstName, setFirstName] = useState("");
	const [lastName, setLastName] = useState("");

	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");

	const [emailDirty, setEmailDirty] = useState(false);
	const [passwordDirty, setPasswordDirty] = useState(false);
	const [confirmPasswordDirty, setConfirmPasswordDirty] = useState(false);

	const [emailError, setEmailError] = useState("E-mail field cannot be empty!");
	const [passwordError, setPasswordError] = useState(
		"Password field cannot be empty!"
	);
	const [confirmPasswordError, setConfirmPasswordError] = useState(
		"Confirm password field cannot be empty!"
	);

	const emailHandler = (e) => {
		if (e.target.value !== 0) {
			const rgx =
				/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			const currentEmail = e.target.value;
			setEmail(currentEmail);
			if (!rgx.test(String(currentEmail).toLowerCase())) {
				setEmailError("Incorrect E-mail");
				if (!e.target.value.length) {
					setEmailError("E-mail field cannot be empty!");
				}
			} else {
				setEmailError("");
			}
		}
		setEmailDirty(true);
	};

	const passwordHandler = (e) => {
		if (e.target.value !== 0) {
			const currentPass = e.target.value;
			setPassword(currentPass);
			if (currentPass.length < 6 || currentPass.length > 8) {
				setPasswordError("Password less 6 or large 8 chars!");
				if (!e.target.value.length) {
					setPasswordError("Password field cannot be empty!");
				}
			} else {
				setPasswordError("");
			}
			if (currentPass !== confirmPassword) {
				setConfirmPasswordError("Confirm password and password isn't equals!");
			} else {
				setConfirmPasswordError("");
			}
		}
		setPasswordDirty(true);
	};

	const confirmPasswordHandler = (e) => {
		if (e.target.value !== 0) {
			const currentConfirmPass = e.target.value;
			setConfirmPassword(currentConfirmPass);
			if (currentConfirmPass !== password) {
				setConfirmPasswordError("Confirm password and password isn`t equals!");
				if (!e.target.value.length)
					setConfirmPasswordError("Confirm password field cannot be empty!");
			} else {
				setConfirmPasswordError("");
			}
		}
		setConfirmPasswordDirty(true);
	};

	return {
		email,
		setEmail,
		password,
		setPassword,
		emailDirty,
		setEmailDirty,
		passwordDirty,
		setPasswordDirty,
		emailError,
		setEmailError,
		passwordError,
		setPasswordError,
		emailHandler,
		passwordHandler,
		confirmPassword,
		confirmPasswordDirty,
		confirmPasswordError,
		confirmPasswordHandler,
		setConfirmPasswordDirty,
		setConfirmPassword,
		firstName,
		setFirstName,
		lastName,
		setLastName,
	};
};
