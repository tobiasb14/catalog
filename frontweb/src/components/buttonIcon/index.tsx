import './styles.css';
import {ReactComponent as Arrow} from 'Assets/Images/seta.svg';

const ButtonIcon = () => {
    return (
        <div className="btn-container">
            <button className="btn btn-primary">
            <h6>INICIE SUA BUSCA AGORA</h6>
            </button>
            <div className="btn-icon-container">
                <Arrow />
            </div>
        </div>
    );
}

export default ButtonIcon;