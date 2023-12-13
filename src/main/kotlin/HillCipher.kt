import java.util.*

object HillCipher {
    private fun textToMatrix(text: String, row: Int, col: Int): Array<IntArray> {
        val matrix = Array(row) { IntArray(col) }
        var index = -1
        for (i in 0..<row) for (j in 0..<col) matrix[i][j] = text[++index].code - 65
        return matrix
    }

    private fun multiplyMatrix(firstMatrix: Array<IntArray>, secondMatrix: Array<IntArray>): Array<IntArray> {
        val row1 = firstMatrix.size
        val col1 = firstMatrix[0].size
        val col2 = secondMatrix[0].size
        val product = Array(row1) { IntArray(col2) }
        for (i in 0..<row1) for (j in 0..<col2) for (k in 0..<col1) product[i][j] =
            product[i][j] + firstMatrix[i][k] * secondMatrix[k][j]
        return product
    }

    /**
     * Converts numbers into corresponding character acc. to ASCII value.
     */
    private fun convertCharacter(matrix: Array<IntArray>): String {
        var result = ""
        val row = matrix.size
        val col = matrix[0].size

        for (i in 0..<row) {
            for (j in 0..<col) {
                val c = (matrix[i][j] % 26 + 65).toChar()
                result += c
            }
        }

        return result
    }

    private fun displayResult(matrix: Array<IntArray>, type: String) {
        val row = matrix.size
        val col = matrix[0].size

        println("\n$type\n----------")
        for (i in 0..<row) {
            for (j in 0..<col) print(String.format("%02d", matrix[i][j] % 26) + "  ")
            println()
        }
        println()
    }

    fun encrypt(textMatrix: Array<IntArray>, keyMatrix: Array<IntArray>): Array<IntArray> {
        val product = multiplyMatrix(textMatrix, keyMatrix)
        displayResult(textMatrix, "Plain Text")
        displayResult(keyMatrix, "Key")
        displayResult(product, "Product")
        println("Equivalent text : " + convertCharacter(product))
        return product
    }

    fun decrypt(textMatrix: Array<IntArray>, keyMatrix: Array<IntArray>): Array<IntArray> {
        val determinant = keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0]
        val adjoint = Array(2) { IntArray(2) }

        adjoint[0][0] = keyMatrix[1][1]
        adjoint[0][1] = -keyMatrix[0][1]
        adjoint[1][0] = -keyMatrix[1][0]
        adjoint[1][1] = keyMatrix[0][0]
        displayResult(textMatrix, "Cipher Text")

        println("\nInverse of key matrix\n-------------")
        for (i in adjoint) {
            for (j in i) {
                print("$j/$determinant  ")
            }
            println()
        }

        println("\n\nMultiplying cipher text with inverse of key...")
        val product = multiplyMatrix(textMatrix, adjoint)
        for (i in product.indices) {
            for (j in product[0].indices) {
                product[i][j] /= determinant
            }
        }

        displayResult(product, "Decrypted/Plain Text")
        println("Equivalent text : " + convertCharacter(product))
        return product
    }

    fun demo(input: String, key: String) {
        var text = input.uppercase(Locale.getDefault())
        text = text.replace("\\s".toRegex(), "")
        if (text.length % 2 != 0) text += 'Z'

        val n = text.length
        val keyMatrix = textToMatrix(key, 2, 2)
        val textMatrix = textToMatrix(text, (n + 1) / 2, 2)

        println("Inputted text : $text")
        println("Chosen key : $key")

        println()

        println("> Encryption:")
        val cipherTextMatrix = encrypt(textMatrix, keyMatrix)
        val cipherText = convertCharacter(cipherTextMatrix)

        println()

        println("> Decryption:")
        val decryptedTextMatrix = decrypt(cipherTextMatrix, keyMatrix)
        val decryptedText = convertCharacter(decryptedTextMatrix)

        println()

        println("> Results: ")
        println("Inputted text : $text")
        println("Ciphered text : $cipherText")
        println("Decrypted text : $decryptedText")
    }
}