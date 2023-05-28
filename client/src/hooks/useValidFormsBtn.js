import { useState } from "react";

export const useValidFormsBtn = () => {
	const [formValid, setFormValid] = useState(false);
	return {
		formValid,
		setFormValid,
	};
};
