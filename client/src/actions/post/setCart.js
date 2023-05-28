import axios from "axios";

export const setCart = async (idProduct, likes, accessToken) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/product-like/set-like-product`,
			{ product_id: idProduct, likes },
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
	} catch (error) {
		// console.log(error);
		throw error; // пробрасываем ошибку выше для обработки в setLikes
	}
};
