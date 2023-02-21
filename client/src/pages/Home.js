import React from "react";
import AboutUs from "../components/ui/about/AboutUs";
import Helmet from "../components/helmet/Helmet";
import Slider from "../components/slider/Slider";
import Category from "../components/ui/category/Category";
import MenuProducts from "../components/ui/menu-products/MenuProducts";
import WhyWe from "../components/ui/why-we/WhyWe";
import HotFood from "../components/ui/hot-food/HotFood";
const Home = () => {
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
