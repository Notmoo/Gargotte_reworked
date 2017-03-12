package com.cess.gargotte.core.order_logging;

import com.cess.gargotte.core.model.products.IReadOnlyProduct;
import com.cess.gargotte.core.model.sales.Order;

import java.io.IOException;
import java.util.List;

/**
 * Created by Guillaume on 19/02/2017.
 */
public interface IOrderLoggingHandler {
    
    /**
     * Ecrit la commande {@code order} dans le fichier de logs des ventes.
     * Si le fichier n'existe pas, il est créé.
     *
     * @param order
     * @return
     */
    boolean write (Order order);
    
    /**
     * <p>Compte le nombre de commandes contenues dans le fichier de order_logging et renvoie
     * le résultat sous forme d'un entier de type long. Si le résultat est supérieur
     * à {@link Long#MAX_VALUE}, alors {@link Long#MAX_VALUE} est renvoyé. Le nombre
     * renvoyé ne peut pas être inférieur à 0.</p>
     *
     * @return
     */
    long count() throws IOException;
    
    /**
     * <p>Lit toutes les commandes qui ont été ajoutées au fichier de logs des ventes,
     * Si le nombre de commandes contenues dans le fichier est important, cette
     * méthode peut potentiellement mettre en danger l'intégrité de la JVM.</p>
     *
     * @param products la liste des produits. Elle permet d'établir une correspondance
     *                 avec les produits grâce aux données contenues dans les logs.
     *                 Cela signifie qu'aucun nouvel objet IProduit ne sera instancié
     *                 durant la lecture des logs, et cela signifie également que si un
     *                 produit lu ne se trouve pas dans la liste, il ne pourra pas être
     *                 identifié et des valeurs d'erreurs seront mises à la place.
     * @return
     * @throws IOException
     */
    List<Order> read(List<IReadOnlyProduct> products) throws IOException;
    
    /**
     * <p>Lit les {@code number} dernières commandes qui ont été ajoutées au fichier de logs des ventes.</p>
     *
     * @param products la liste des produits. Elle permet d'établir une correspondance
     *                 avec les produits grâce aux données contenues dans les logs.
     *                 Cela signifie qu'aucun nouvel objet IProduit ne sera instancié
     *                 durant la lecture des logs, et cela signifie également que si un
     *                 produit lu ne se trouve pas dans la liste, il ne pourra pas être
     *                 identifié et des valeurs d'erreurs seront mises à la place.
     * @param number le nombre de commandes à lire. Les commandes renvoyées sont les commandes
     *               les plus récentes, et donc à la fin du fichier.
     * @return
     * @throws IOException
     */
    List<Order> read(List<IReadOnlyProduct> products, long number) throws IOException;
}
