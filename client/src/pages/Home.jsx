import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import AboutUs from "../components/ui/home-ui-components/about/AboutUs";
import Helmet from "../components/helmet/Helmet";
import Slider from "../components/ui/home-ui-components/slider/Slider";
import Category from "../components/ui/home-ui-components/category/Category";
import MenuProducts from "../components/ui/home-ui-components/menu-products/MenuProducts";
import WhyWe from "../components/ui/home-ui-components/why-we/WhyWe";
import HotFood from "../components/ui/home-ui-components/hot-food/HotFood";

import { axiosGetStatusLikes } from "../redux/store/shopping-cart/cartsLikedSlice";

const Home = () => {
	const dispatch = useDispatch();
	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const accessToken = useSelector((state) => state.user.accessToken);

	useEffect(() => {
		window.scrollTo(0, 0);
	}, []);

	useEffect(() => {
		console.log("памагити");
		if (isAuthenticated) {
			dispatch(
				axiosGetStatusLikes({
					accessToken: accessToken,
				})
			);
		}
	}, [accessToken]);

	return (
		<>
			<Helmet title="Home">
				<Slider />
				<section className="section__container">
					<Category />
				</section>
				<section className="section__container">
					<AboutUs />
				</section>
				<section className="section__container">
					<MenuProducts />
				</section>
				<section className="section__container">
					<WhyWe />
				</section>
				<section className="section__container">
					<HotFood />
				</section>
			</Helmet>
		</>
	);
};

export default Home;
