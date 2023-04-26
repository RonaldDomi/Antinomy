//package Controleur;
///*
// * Morpion pédagogique
//
// * Copyright (C) 2016 Guillaume Huard
//
// * Ce programme est libre, vous pouvez le redistribuer et/ou le
// * modifier selon les termes de la Licence Publique Générale GNU publiée par la
// * Free Software Foundation (version 2 ou bien toute autre version ultérieure
// * choisie par vous).
//
// * Ce programme est distribué car potentiellement utile, mais SANS
// * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
// * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
// * Licence Publique Générale GNU pour plus de détails.
//
// * Vous devez avoir reçu une copie de la Licence Publique Générale
// * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
// * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
// * États-Unis.
//
// * Contact: Guillaume.Huard@imag.fr
// *          Laboratoire LIG
// *          700 avenue centrale
// *          Domaine universitaire
// *          38401 Saint Martin d'Hères
// */
//
//import java.util.Random;
//import Structures.Jeu;
//import Structures.Position;
//
//class JoueurIA extends Joueur {
//    Random r;
//
//    JoueurIA(int n, Jeu p) {
//        super(n, p);
//        r = new Random();
//    }
//
//    @Override
//    boolean tempsEcoule() {
//        if (plateau.gagnant() == -1) {
//            // Pour cette IA, on selectionne aléatoirement une case libre
//            /*
//			int i, j;
//
//			i = r.nextInt(plateau.plateau().get_nb_lignes());
//			j = r.nextInt(plateau.plateau().get_nb_lignes());
//			while (!plateau.plateau().get_tableau(i, j).est_vide()) {
//				i = r.nextInt(plateau.plateau().get_nb_lignes());
//				j = r.nextInt(plateau.plateau().get_nb_lignes());
//			}
//			*/
//
//
//            if(!aDroit(0, 0)){
//                if (aBas(0,0))
//                    return plateau.jouer_coup(1,0);
//                else
//                    return plateau.jouer_coup(0,0);
//            }
//            else if ((!aBas(0,0)))
//                return plateau.jouer_coup(0, 1);
//
//            //position du poison (0, 0)
//            Position p = getBestPos(0,0);
//
//            return plateau.jouer_coup(p.ligne, p.colonne);
//
//        }
//        return false;
//
//    }
//
//    //ici on voit que pour gagner il faut ne pas supprimer en premier toute la partie droite ou basse
//    //une fois que l'adversaire supprime une des partie on supprime l autre
//    //et il nous reste ensuite que le poison donc on gagne
//    //on uitilise la methode diviser pour regner
//    //et des fonctions a bas et a droit
//
//    Position getBestPos(int i, int j){
//
//        if (aDroit(i,j)){
//            return getBestPos(i,j+1);
//        } else if (aBas(i,j)) {
//            return getBestPos(i+1, j);
//        } else
//            return new Position(i,j);
//    }
//
//
//    Boolean aBas(int i, int j){
//        if (i+1 < plateau.plateau().get_nb_lignes())
//            return plateau.plateau().get_tableau(i+1,j).est_vide();
//        else
//            return false;
//    }
//
//    Boolean aDroit(int i, int j){
//        if (j+1 < plateau.plateau().get_nb_colonnes())
//            return plateau.plateau().get_tableau(i,j+1).est_vide();
//        else
//            return false;
//    }
//}