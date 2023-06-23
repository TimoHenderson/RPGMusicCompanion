import styled from "styled-components";
import headerBg from "../../images/header-bg.jpeg";

export const TwoColGrid = styled.div`
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
    justify-content: center;
`;

export const ThreeColGrid = styled.div`
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 1rem;
    justify-content: center;
`;

export const CentredFlexColumn = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 1rem;
`;

export const HeaderDiv = styled.div`
display: grid;
/* background-image: url(${headerBg}); */
background-color: #222222DD;
grid-template-columns: 1fr 1fr 1fr;
height: 7rem;

align-items: center;
width: 100%;
padding-left: 2rem;
padding-right: 2rem;
`;

export const ControlPanel = styled.div`         
display: flex;
flex-wrap: wrap;
align-items: center;
justify-content: center;
gap: 2rem;
background-color: #555555DD;
border: 2px outset #555555;
padding: 2rem;
`;
