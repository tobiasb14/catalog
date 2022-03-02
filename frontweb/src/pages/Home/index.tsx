import { ReactComponent as MainImage } from "Assets/Images/main-image.svg";
import ButtonIcon from "components/buttonIcon";
import Navbar from "components/Navbar";
import './styles.css';

function Home() {
    return (
      <>
        <Navbar />
        <div className="home-container">
            <div className="base-card home-card">
                <div className="home-content-container">
                    <div>
                    <h1>Conheça o melhor catálogo de produtos</h1>
                    <p>Ajudaremos você a encontrar os melhores produtos no mercado</p>
                    </div>
                    <ButtonIcon />
                </div>
                <div className="home-image-container">
                    <MainImage />
                </div>
            </div>
        </div>
      </>
    );
  }
  
  export default Home;