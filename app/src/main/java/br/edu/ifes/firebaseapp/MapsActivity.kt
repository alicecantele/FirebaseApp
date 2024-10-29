package br.edu.ifes.firebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

import br.edu.ifes.firebaseapp.databinding.ActivityMapsBinding

import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapsActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMapsBinding

    private var position: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialize o View Binding
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa a configuração do mapa
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        // Configura o MapView usando binding
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(15.0)
        
        // Define um ponto inicial (latitude, longitude)
        val latitude = intent.getDoubleExtra("latitude", Double.NaN)
        val longitude = intent.getDoubleExtra("longitude", Double.NaN)
        position = GeoPoint(latitude, longitude)
        binding.mapView.controller.setCenter(position)

        // Adiciona um marcador no ponto inicial
        val marker = Marker(binding.mapView)
        marker.position = position
        marker.infoWindow = null
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        // Permitir arrastar o marcador
        marker.isDraggable = true
        marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker?) {}
            override fun onMarkerDrag(marker: Marker?) {}

            override fun onMarkerDragEnd(marker: Marker?) {
                marker?.let {
                    position = it.position
                }
            }
        })

        binding.mapView.overlays.add(marker)

        // Botão para confirmar o local
        binding.confirmLocationButton.setOnClickListener {
            position?.let {
                val resultIntent = Intent()
                resultIntent.putExtra("latitude", it.latitude)
                resultIntent.putExtra("longitude", it.longitude)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

    }

}