import React, { useState, useEffect } from 'react';
import { obtenerFavoritos, eliminarFavorito } from '../../../services/favoritoService';
import { useToast } from '../../../context/ToastContext';
import Loader from '../../../components/ui/Loader/Loader';

const FavoritosPages = () => {
    const [favoritos, setFavoritos] = useState([]);
    const [loading, setLoading] = useState(true);
    const { addToast } = useToast();

    useEffect(() => {
        cargarFavoritos();
    }, []);

    const cargarFavoritos = async () => {
        setLoading(true);
        try {
            const data = await obtenerFavoritos();
            setFavoritos(data);
        } catch (error) {
            addToast('error', error.message || 'Error al cargar favoritos');
            setFavoritos([]);
        } finally {
            setLoading(false);
        }
    };

    const handleEliminarFavorito = async (idPalabra) => {
        if (!window.confirm('¿Estás seguro de eliminar esta palabra de favoritos?')) {
            return;
        }

        try {
            await eliminarFavorito(idPalabra);
            addToast('success', 'Palabra eliminada de favoritos');
            setFavoritos(favoritos.filter(fav => fav.idPalabra !== idPalabra));
        } catch (error) {
            addToast('error', error.message || 'Error al eliminar favorito');
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader />
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <div className="mb-6">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">
                    Mis Palabras Favoritas
                </h1>
                <p className="text-gray-600">
                    Tienes {favoritos.length} palabra{favoritos.length !== 1 ? 's' : ''} guardada{favoritos.length !== 1 ? 's' : ''}
                </p>
            </div>

            {favoritos.length === 0 ? (
                <div className="text-center py-12 bg-white rounded-lg shadow">
                    <div className="text-6xl mb-4">❤️</div>
                    <h2 className="text-2xl font-semibold text-gray-700 mb-2">
                        No tienes favoritos aún
                    </h2>
                    <p className="text-gray-500">
                        Comienza a guardar tus palabras favoritas en Nasa Yuwe
                    </p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {favoritos.map((favorito) => (
                        <div
                            key={favorito.idFavorito}
                            className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
                        >
                            {favorito.imagenUrl && (
                                <div className="h-48 overflow-hidden bg-gray-100">
                                    <img
                                        src={favorito.imagenUrl}
                                        alt={favorito.palabraNasa}
                                        className="w-full h-full object-cover"
                                    />
                                </div>
                            )}
                            
                            <div className="p-4">
                                <div className="mb-2">
                                    {favorito.categoria && (
                                        <span className="inline-block bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full mb-2">
                                            {favorito.categoria}
                                        </span>
                                    )}
                                </div>
                                
                                <h3 className="text-xl font-bold text-gray-800 mb-1">
                                    {favorito.palabraNasa}
                                </h3>
                                
                                <p className="text-gray-600 mb-3">
                                    {favorito.traduccionEspanol}
                                </p>
                                
                                <div className="flex justify-between items-center pt-3 border-t">
                                    <span className="text-xs text-gray-500">
                                        {favorito.fechaAgregado}
                                    </span>
                                    
                                    <button
                                        onClick={() => handleEliminarFavorito(favorito.idPalabra)}
                                        className="text-red-500 hover:text-red-700 transition"
                                        title="Eliminar de favoritos"
                                    >
                                        <i className="bi bi-heart-fill text-xl"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default FavoritosPages;