package pl.mimuw.zpp.quantumai.backendui.service

import org.springframework.http.ResponseEntity
import pl.mimuw.zpp.quantumai.backendui.feign.UtmPythonFeignClient
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph
import pl.mimuw.zpp.quantumai.backendui.repository.EuclideanGraphRepository
import spock.lang.Specification

import java.math.MathContext

import static java.math.BigDecimal.valueOf

class SolveServiceTest extends Specification {
    EuclideanGraphRepository euclideanGraphRepository = Mock()
    UtmPythonFeignClient utmPythonFeignClient = Mock()
    SolveService solveService

    def setup() {
        solveService = new SolveService(euclideanGraphRepository, utmPythonFeignClient)
    }

    def "should return error when graph with given name is not in the DB"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.empty()

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when turing machine call was unsuccessful"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity<String>.internalServerError().build()

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when permutation has wrong size"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("0")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when first vertex is not equal 0"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("1 1 2 0")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when last vertex is not equal 0"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("0 1 2 1")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when permutation has vertices bigger than number of vertices"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("0 1 3 0")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when permutation has vertices smaller than 0"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("0 1 -1 0")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return error when permutation has duplicates"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("0 1 1 0")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isLeft()
    }

    def "should return weight of permutation if all goes well"() {
        given:
        def pythonCode = "some python code"
        def graphName = sampleGraphName()
        euclideanGraphRepository.findById(graphName) >> Optional.of(sampleGraph())
        utmPythonFeignClient.solve(pythonCode, sampleGraphAsString()) >> ResponseEntity.ok("0 1 2 0")

        when:
        def result = solveService.solveTsp(pythonCode, graphName)

        then:
        result.isRight()
        result.get() == valueOf(1.25).sqrt(MathContext.DECIMAL64).multiply(valueOf(2)).add(valueOf(0.5).sqrt(MathContext.DECIMAL64))
    }

    private def sampleGraphName() {
        return "sampleGraph"
    }

    private def sampleGraph() {
        return EuclideanGraph.builder()
            .name(sampleGraphName())
            .nodes([
                    EuclideanGraph.Node.builder()
                        .x(valueOf(0))
                        .y(valueOf(0))
                        .build(),
                    EuclideanGraph.Node.builder()
                            .x(valueOf(1))
                            .y(valueOf(0.5))
                            .build(),
                    EuclideanGraph.Node.builder()
                            .x(valueOf(0.5))
                            .y(valueOf(1))
                            .build()
            ])
            .build()
    }

    private def sampleGraphAsString() {
        return "3\n0 0\n1 0.5\n0.5 1"
    }
}
