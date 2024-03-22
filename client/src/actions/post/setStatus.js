import axios from "axios";

export const setStatus = async (idProduct, status, accessToken) => {
	try {
		await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/product-like/set-status-product`,
			{ product_id: idProduct, status },
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
	} catch (error) {
		// console.error(error);
		throw error; // пробрасываем ошибку выше для обработки в setLikes
	}
};
