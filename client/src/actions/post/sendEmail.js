import axios from "axios";

export const sendEmail = async (mailData) => {
	try {
		const result = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/email/contact-review`,
			mailData
		);
		return result;
	} catch (error) {
		throw error;
	}
};
