package com.konchak.cnc_halper.domain.models

/**
 * Модель инструмента от производителя (Sandvik, Kennametal, МИЗ и др.)
 * Используется для поиска в базах производителей и автозаполнения данных
 */
data class ManufacturerTool(
    val id: String = "", // ID в базе производителя
    val name: String = "", // Полное название
    val code: String = "", // Артикул/код производителя
    val type: String = "", // Тип инструмента
    val manufacturer: String = "", // Название производителя
    val country: String = "", // Страна производителя
    val specifications: ToolSpecifications = ToolSpecifications(),
    val cuttingParameters: Map<String, CuttingParameters> = emptyMap(), // Параметры для разных материалов
    val imageUrl: String? = null, // URL изображения
    val datasheetUrl: String? = null, // URL техпаспорта
    val price: Double? = null, // Примерная цена
    val deliveryTime: Int? = null // Срок поставки в днях
) {
    // Конвертация в доменную модель Tool
    fun toDomainTool(operatorId: String): Tool {
        return Tool(
            name = this.name,
            type = this.type,
            diameter = this.specifications.diameter ?: 0f,
            length = this.specifications.length ?: 0f,
            material = this.specifications.material,
            coating = this.specifications.coating,
            operatorId = operatorId,
            notes = "Производитель: $manufacturer\nКод: $code"
        )
    }

    // Получить параметры резания для конкретного материала
    fun getCuttingParametersForMaterial(material: String): CuttingParameters? {
        return cuttingParameters[material] ?: cuttingParameters.values.firstOrNull()
    }
}

/**
 * Технические характеристики инструмента от производителя
 */
data class ToolSpecifications(
    val diameter: Float? = null, // Диаметр, мм
    val length: Float? = null, // Длина, мм
    val material: String = "", // Материал
    val coating: String = "", // Покрытие
    val shankDiameter: Float? = null, // Диаметр хвостовика
    val fluteCount: Int? = null, // Количество зубьев
    val helixAngle: Float? = null, // Угол наклона спирали
    val pointAngle: Float? = null, // Угол при вершине (для сверл)
    val tolerance: String = "" // Допуск
)